package ru.skillbranch.devintensive.models.data

import android.icu.text.CaseMap
import android.util.Log
import ru.skillbranch.devintensive.extensions.format
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.extensions.truncate
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
        val id: String,
        val title: String,
        val members: List<User> = listOf(),
        var messages: MutableList<BaseMessage> = mutableListOf(),
        var isArchived: Boolean = false
) {
        fun unreadableMessageCount(): Int = messages.size

        fun lastMessageDate(): Date? {
                return if (messages.isEmpty()){
                        null
                }else{
                        messages.last().date
                }
        }

        private fun lastMessageShort(): Pair<String, String>{
                if (messages.isEmpty()) {
                        return "Сообщений еще нет" to "@John_Doe"
                }else {
                        val lastMessage = messages.last()
                        val from = lastMessage.from?.firstName + lastMessage.from?.lastName
                        val message = lastMessage.getMessage() ?: ""
                        var pair: Pair<String,String> = "" to ""
                        when(lastMessage.format()){
                                "Image" -> pair ="${lastMessage.from?.firstName}- отправил фото" to from
                                "Text" -> pair = message.truncate() to from
                        }
                        return pair
                }
        }

        private fun isSingle(): Boolean = members.size == 1

        fun toChatItem(): ChatItem {
                return if (isSingle()){
                        val user = members.first()
                        ChatItem(
                                id,
                                user.avatar,
                                Utils.toInitials(user.firstName, user.lastName) ?: "??",
                                "${user.firstName ?: ""} ${user.lastName ?: ""}",
                                lastMessageShort().first,
                                unreadableMessageCount(),
                                lastMessageDate()?.shortFormat(),
                                user.isOnline
                        )
                }else {
                        ChatItem(
                                id,
                                null,
                                "",
                                title,
                                lastMessageShort().first,
                                unreadableMessageCount(),
                                lastMessageDate()?.shortFormat(),
                                false,
                                ChatType.GROUP,
                                lastMessageShort().second
                        )
                }

        }
}

enum class ChatType{
        SINGLE,
        GROUP,
        ARCHIVE
}
