//package ru.skillbranch.devintensive.models
//
//import android.util.Log
//import ru.skillbranch.devintensive.ui.custom.AvatarInitialsDrawable
//import ru.skillbranch.devintensive.utils.Utils
//
//data class Profile(
//        val firstName: String,
//        val lastName: String,
//        val about: String,
//        val repository: String,
//        val rating: Int = 0,
//        val respect: Int = 0
//) {
//    val nickName: String = Utils.transliteration("$firstName $lastName","_")
//    val rank: String = "Junior Android Developer"
//
//    fun toMap(): Map<String, Any> = mapOf(
//        "nickName" to nickName,
//        "rank" to rank,
//        "firstName" to firstName,
//        "lastName" to lastName,
//        "about" to about,
//        "repository" to repository,
//        "rating" to rating,
//        "respect" to respect
//    )
//
//    fun getDefaultAvatar(charColor:Int, backgroundColor:Int) : AvatarInitialsDrawable? {
//        var initials = Utils.toInitials(firstName, lastName)
//        initials = Utils.transliteration(initials,"")
//
//        Log.d("M_Profile","getDefaultAvatar initials: $initials")
//
//        if(initials.isNullOrEmpty()) {
//            return null
//        }
//
//        return AvatarInitialsDrawable(initials, charColor, backgroundColor)
//
//    }
//}