package ru.skillbranch.devintensive.ui.profile

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity(){

    companion object{
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    var isEditeMode = false
    lateinit var viewFields: Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
        Log.d("M_ProfileActivity","onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_EDIT_MODE,isEditeMode)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun updateTheme(mode: Int) {
        Log.d("M_ProfileActivity","updateTheme")
        delegate.setLocalNightMode(mode)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for ((k,v) in viewFields){
                v.text = it[k].toString()
            }
        }
        updateAvatar(profile)
    }

    private fun initViews(savedInstanceState: Bundle?) {
        viewFields = mapOf(
                "nickName" to tv_nick_name,
                "rank" to tv_rank,
                "firstName" to et_first_name,
                "lastName" to et_last_name,
                "about" to et_about,
                "repository" to et_repository,
                "rating" to tv_rating,
                "respect" to tv_respect
        )

        isEditeMode = savedInstanceState?.getBoolean(IS_EDIT_MODE,false) ?: false
        showCurrentMode(isEditeMode)

        btn_edit.setOnClickListener{
            if (isEditeMode) {
                if (wr_repository.isErrorEnabled) {
                    et_repository.text.clear()
                    wr_repository.isErrorEnabled = false
                    wr_repository.error = null
                }
                saveProfileInfo()
            }
            isEditeMode = !isEditeMode
            showCurrentMode(isEditeMode)
        }

        btn_switch_theme.setOnClickListener(View.OnClickListener {
            viewModel.switchTheme()
        })

        et_repository.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                wr_repository.error = null
                wr_repository.isErrorEnabled = !validateRepository()
                if (wr_repository.isErrorEnabled){
                    wr_repository.error = "Невалидный адрес репозитория"
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private fun showCurrentMode(isEdite: Boolean) {
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
        for ((_,v) in info){
            v as EditText
            v.isFocusable =isEdite
            v.isFocusableInTouchMode = isEdite
            v.isEnabled = isEdite
            v.background.alpha = if (isEdite) 255 else 0
        }

        ic_eye.visibility = if (isEdite) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdite

        with(btn_edit){
            val filter: ColorFilter? = if(isEdite) {
                PorterDuffColorFilter(
                        resources.getColor(R.color.color_accent, theme),
                        PorterDuff.Mode.SRC_IN
                )
            }else {
                null
            }

            val icon = if(isEdite) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileInfo() {
        Profile(
                firstName = et_first_name.text.toString(),
                lastName = et_last_name.text.toString(),
                about = et_about.text.toString(),
                repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

    @ColorInt
    fun getColorByAttributeId(@AttrRes attrIdForColor: Int): Int {
        val typedValue = TypedValue()
        theme.resolveAttribute(attrIdForColor, typedValue, true)
        return typedValue.data
    }

    private fun updateAvatar(profile: Profile) {

        val colorAccent = getColorByAttributeId(R.attr.colorAccent)
        val colorFont = getColorByAttributeId(R.attr.colorWhite)

        val avatar = profile.getDefaultAvatar(colorFont, colorAccent)
        if (null != avatar) {
            iv_avatar.setImageDrawable(avatar)
        }
    }

    fun validateRepository(): Boolean {
        if (et_repository.text.isNullOrEmpty()){
            return true
        }

        val regexRepository = Regex("^(https?://)?(w{3}\\.)?github\\.com/(?<repoName>[a-zA-Z0-9]+-?[a-zA-Z0-9]+)/?$")
        val repositoryName = regexRepository.find(et_repository.text)?.groups?.get(3)?.value

        val excludePaths = listOf("enterprise", "features", "topics", "collections",
                "trending", "events", "marketplace", "pricing", "nonprofit", "customer-stories",
                "security", "login", "join")

        if (!repositoryName.isNullOrEmpty() && !excludePaths.contains(repositoryName)) {
            return true
        }
        return false
    }
}