package ru.obolonnyy.friendhelper.main.bottomlogs

import kotlinx.coroutines.channels.ConflatedBroadcastChannel

class LogsViewModel {

    val text = ConflatedBroadcastChannel<List<String>>()
    var localText = mutableListOf<String>()

    fun putSomeError(error: String) {
        localText.add(error)
        text.offer(localText)
    }

     fun clear() {
        text.offer(emptyList())
        localText = mutableListOf()
    }
}