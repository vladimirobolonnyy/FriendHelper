package ru.obolonnyy.friendhelper.utils

import com.google.gson.Gson
import retrofit2.HttpException
import timber.log.Timber


data class BaseBean1(val arg0: String = "email", val arg1: String = "pass")

data class AuthServiceError(
        val httpErrno: Int?,
        val errmsg: String?,
        val exceptionClass: String?
)

fun HttpException.parseAsAuthService(): AuthServiceError? {
    return try {
        Gson().fromJson(response().errorBody()!!.charStream(), AuthServiceError::class.java)
    } catch (e: Throwable) {
        Timber.e(e, "HttpException.parseAsAuthService()")
        null
    }
}