package ru.obolonnyy.friendhelper.main.bottomlogs

import kotlinx.coroutines.channels.Channel

class LogsViewModel {

    val text = Channel<List<String?>>(10)
    var localText = mutableListOf<String>()

    suspend fun putSomeError(error: String) {
        localText.add(error)
        text.offer(localText)
    }

    suspend fun clear() {
        text.offer(emptyList())
        localText = mutableListOf()
    }
}