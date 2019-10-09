package ru.obolonnyy.friendhelper.utils.data

sealed class MyResult<out T> {

    abstract val stringResult: String

    class Success<T>(val data: T) : MyResult<T>() {
        override val stringResult = data.toString()
    }

    class Error(exception: Exception?= null, message: String? = null) : MyResult<Nothing>() {
        override val stringResult = message ?: exception?.message ?: exception?.localizedMessage ?: "empty message"
    }
}

inline fun <T> asResult(block: () -> T): MyResult<T> {
    return try {
        MyResult.Success(block())
    } catch (ex: Exception) {
        MyResult.Error(ex)
    }
}