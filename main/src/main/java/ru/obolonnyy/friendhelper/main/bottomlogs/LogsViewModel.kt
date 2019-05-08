package ru.obolonnyy.friendhelper.main.bottomlogs

import kotlinx.coroutines.channels.ConflatedBroadcastChannel

class LogsViewModel {

    private val localText = mutableListOf<String>()
    val text = ConflatedBroadcastChannel<List<String>>()

    fun putSomeLogs(message: String) {
        localText.add(message)
        text.offer(localText)
    }

     fun clear() {
         localText.clear()
         text.offer(localText)
    }
}