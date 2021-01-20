package ru.skillbranch.devintensive.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class ArchiveViewModel: ViewModel() {
    private val chatRepository = ChatRepository
    private val archiveChats = Transformations.map(chatRepository.loadChats()){ archiveChats ->
        return@map archiveChats.filter { it.isArchived }
                .map{it.toChatItem()}
                .sortedBy { it.id.toInt() }
    }

    fun getArchiveData(): LiveData<List<ChatItem>>{
        Log.d("M_ArchiveViewModel","getArchiveData() $archiveChats")
        return archiveChats
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
}