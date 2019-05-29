package ru.obolonnyy.friendhelper.utils.data

sealed class MyResult<out T : Any> {

    abstract fun stringResult() : String

    class Success<T : Any>(val data: T) : MyResult<T>() {
        override fun stringResult() = data.toString()
    }

    class Error(val exception: Exception, val message: String) : MyResult<Nothing>() {
        override fun stringResult() = message
    }
}