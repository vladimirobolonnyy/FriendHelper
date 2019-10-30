package ru.obolonnyy.friendhelper.main

import java.io.File

data class MainViewState(
    var items: List<StandState>? = null
)

sealed class MainViewEvent {
    data class OpenFile(val file: File) : MainViewEvent()
}

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}