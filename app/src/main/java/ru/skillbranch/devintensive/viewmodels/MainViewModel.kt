package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.lifecycle.*
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.UserItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel: ViewModel() {
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()){ chats ->
        return@map chats.filter { !it.isArchived }
                .map{it.toChatItem()}
                .sortedBy { it.id.toInt() }
    }

    private val archive = Transformations.map(chatRepository.loadChats()){archive ->
        return@map archive.filter { it.isArchived }
    }

    fun getChatData() :LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chats = chats.value!!

            result.value = if (queryStr.isEmpty()) chats
            else chats.filter { it.title.contains(queryStr, true) }
        }

        result.addSource(chats){filterF.invoke()}
        result.addSource(query){filterF.invoke()}

        return result
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String){
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String) {
        query.value = text
    }

    fun getArchiveData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()
        val filter = {
            result.value = archive.value!!.map { it.toChatItem() }
        }
        result.addSource(archive){filter.invoke()}
        Log.d("M_MainViewModel","getArchive result = ${result.value} archive = ${archive.value}")

        return result
    }

    fun updateArchive(): Map<String, String?>? {
        if(archive.value.isNullOrEmpty())
            return null
        else {
            val archiveChats = archive.value
            var date = archiveChats!![0].lastMessageDate()
            var count = 0
            var currentChat = archiveChats[0]
            for(chat in archiveChats){
                count += chat.toChatItem().messageCount
                if (chat.lastMessageDate() == null) continue
                if (date == null || chat.lastMessageDate()?.compareTo(date)!! > 0) {
                    date = chat.lastMessageDate()
                    currentChat = chat
                }
            }

            val chatItem = currentChat.toChatItem()
            val lastMessage = chatItem.shortDescription
            val lastMessageAuthor = chatItem.author
            val lastMessageDate = chatItem.lastMessageDate

            Log.d("M_MainViewModel","getArchiveData lastMessageAuthor = $lastMessageAuthor, lastMessage = $lastMessage")
            return mapOf(
                "lastMessage" to lastMessage,
                "lastMessageDate" to lastMessageDate,
                "lastMessageAuthor" to lastMessageAuthor,
                "count" to count.toString()
            )
        }
    }
}