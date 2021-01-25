package ru.skillbranch.devintensive.ui.archive

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.ui.adapters.ChatAdapter
import ru.skillbranch.devintensive.ui.adapters.ChatItemTouchHelperCallback
import ru.skillbranch.devintensive.viewmodels.ArchiveViewModel
import ru.skillbranch.devintensive.viewmodels.MainViewModel

class ArchiveActivity: AppCompatActivity() {
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var viewModel: ArchiveViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("M_ArchiveActivity","onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        initToolbar()
        initViews()
        initViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home){
            finish()
            overridePendingTransition(R.anim.idle, R.anim.bottom_down)
            true
        }else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initViews() {
        Log.d("M_ArchiveActivity","InitViews ")
        chatAdapter = ChatAdapter{
            Snackbar.make(rv_archive_list, "Click on ${it.title}", Snackbar.LENGTH_LONG)
        }
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        val touchCallback = ChatItemTouchHelperCallback(chatAdapter){
            val id = it.id
            viewModel.restoreFromArchive(id)
            val snack = Snackbar.make(rv_archive_list, "Вы точно хотите восстановить ${it.title} из архива?", Snackbar.LENGTH_LONG)
            snack.setAction("ОТМЕНИТЬ", View.OnClickListener {
                viewModel.addToArchive(id)
            })
            snack.show()
        }
        val touchHelper = ItemTouchHelper(touchCallback)

        touchHelper.attachToRecyclerView(rv_archive_list)

        with(rv_archive_list){
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@ArchiveActivity)
            addItemDecoration(divider)
        }
    }

    private fun initViewModel() {
        Log.d("M_ArchiveActivity","initViewModel")
        viewModel = ViewModelProviders.of(this).get(ArchiveViewModel::class.java)
        viewModel.getArchiveData().observe(this, Observer { chatAdapter.updateData(it)})
    }


}