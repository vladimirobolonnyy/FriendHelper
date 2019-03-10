package ru.obolonnyy.friendhelper.utils.data

sealed class MyResult<out T : Any> {

    class Success<T : Any>(val data: T) : MyResult<T>()

    class Error(val exception: Exception, val message: String) : MyResult<Nothing>()
}