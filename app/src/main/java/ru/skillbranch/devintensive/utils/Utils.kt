package ru.skillbranch.devintensive.utils

import ru.skillbranch.devintensive.extensions.truncate

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?>{
        val parts : List<String>? = fullName?.split(" ")

        var firstName = parts?.getOrNull(0)
        when(firstName){
            "", " ", "null" -> firstName = null
        }

        var lastName = parts?.getOrNull(1)
        when(lastName){
            "", " ","null" -> lastName = null
        }

        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        var translit: String =""

        for (liter in payload) {
            if ("$liter" == " "){
                translit += divider
            } else {
                translit += literasMapping(liter)
            }
        }

        return translit
    }

    fun toInitials(firstName: String?, lastName: String?): String?  {
        val initials: String = "${firstName?.truncate()?.getOrNull(0)?.toUpperCase() ?: ""}" + "${lastName?.truncate()?.getOrNull(0)?.toUpperCase() ?: ""}"
        if(initials.isEmpty())
            return null
        return initials
    }

    private fun literasMapping(liter: Char):String {
        val isUpperCase: Boolean = liter.isUpperCase()

        var transLiter: String

        when(liter.toLowerCase()) {
            'а' -> transLiter = "a"
            'б' -> transLiter = "b"
            'в' -> transLiter = "v"
            'г' -> transLiter = "g"
            'д' -> transLiter = "d"
            'е' -> transLiter = "e"
            'ё' -> transLiter = "e"
            'ж' -> transLiter = "zh"
            'з' -> transLiter = "z"
            'и' -> transLiter = "i"
            'й' -> transLiter = "i"
            'к' -> transLiter = "k"
            'л' -> transLiter = "l"
            'м' -> transLiter = "m"
            'н' -> transLiter = "n"
            'о' -> transLiter = "o"
            'п' -> transLiter = "p"
            'р' -> transLiter = "r"
            'с' -> transLiter = "s"
            'т' -> transLiter = "t"
            'у' -> transLiter = "u"
            'ф' -> transLiter = "f"
            'х' -> transLiter = "h"
            'ц' -> transLiter = "c"
            'ч' -> transLiter = "ch"
            'ш' -> transLiter = "sh"
            'щ' -> transLiter = "sh'"
            'ъ' -> transLiter = ""
            'ы' -> transLiter = "i"
            'ь' -> transLiter = ""
            'э' -> transLiter = "e"
            'ю' -> transLiter = "yu"
            'я' -> transLiter = "ya"
            else -> transLiter = "$liter"
        }

        if (isUpperCase){
            transLiter = transLiter.capitalize()
        }
        return transLiter
    }

}