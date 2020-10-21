package ru.skillbranch.devintensive

import org.junit.Test

import org.junit.Assert.*
import ru.skillbranch.devintensive.extensions.format
import ru.skillbranch.devintensive.extensions.toUserView
import ru.skillbranch.devintensive.models.User
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_instance() {
        val user = User( "1")
        val user2 = User( "2", "John", "Wick")
        //val user3 = User( "3", "John", "Silverhead", null, lastVisit = Date(), isOnline = true)

        //user.printMe()
        //user2.printMe()
        //user3.printMe()

        println("$user $user2")
    }

    @Test
    fun test_factory(){
        val user = User.makeUser("John Wick")
        val user2 = User.makeUser("John Senna")

        print(user)
    }

    @Test
    fun test_decomposition(){
        val user = User.makeUser("John Doe")
        fun getUserInfo() = user

        val (id, firstName, lastName) = getUserInfo()

        println("$id $firstName $lastName")
    }

    @Test
    fun test_copy(){
        val user = User.makeUser("John Cena")
        var user2 = user.copy(lastVisit = Date())

        println("""
            ${user2.lastVisit}
            ${user2.lastVisit?.format()}
        """.trimIndent())
    }

    @Test
    fun test_data_mapping(){
        val user = User.makeUser("Kate Cher")
        println(user)

        val userView = user.toUserView()
        userView.printMe()
    }
}