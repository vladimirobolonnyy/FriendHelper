package ru.obolonnyy.friendhelper.utils

import com.google.gson.Gson
import retrofit2.HttpException
import timber.log.Timber


data class BaseBean1(val arg0: String = "email", val arg1: String = "pass")

data class AuthServiceError(val message: String?, val code: Int?, val kind: AuthServerError?)

data class OldAuthServiceError(
    val httpErrno: Int?,
    val errmsg: String?,
    val exceptionClass: String?
)

fun HttpException.parseAuthService(): AuthServiceError? {
    return try {
        Gson().fromJson(response().errorBody()!!.charStream(), AuthServiceError::class.java)
    } catch (e: Throwable) {
        Timber.e(e, "HttpException.parseAsAuthService()")
        null
    }
}

fun HttpException.parseOldAuthService(): OldAuthServiceError? {
    return try {
        Gson().fromJson(response().errorBody()!!.charStream(), OldAuthServiceError::class.java)
    } catch (e: Throwable) {
        Timber.e(e, "HttpException.parseAsAuthService()")
        null
    }
}
