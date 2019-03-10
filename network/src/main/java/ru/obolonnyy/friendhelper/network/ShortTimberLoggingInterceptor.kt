package ru.obolonnyy.friendhelper.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import timber.log.Timber

import java.io.IOException

class ShortTimberLoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        Timber.i("Request:\n ${request.url()}")
        Timber.v("REQUEST BODY BEGIN\n%s\nREQUEST BODY END", bodyToString(request))

        val response = chain.proceed(request)
        val responseBody = response.body()
        val responseBodyString = response.body()!!.string()

        Timber.v("RESPONSE BODY BEGIN:\n%s\nRESPONSE BODY END", responseBodyString)

        // now we have extracted the response body but in the process
        // we have consumed the original reponse and can't read it again
        // so we need to build a new one to return from this method

        return response.newBuilder()
            .body(ResponseBody.create(responseBody!!.contentType(), responseBodyString.toByteArray())).build()
    }

    private fun bodyToString(request: Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }
}
