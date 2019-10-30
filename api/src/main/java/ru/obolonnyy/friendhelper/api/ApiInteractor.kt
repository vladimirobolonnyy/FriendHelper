package ru.obolonnyy.friendhelper.api

import okhttp3.ResponseBody
import retrofit2.Call

interface ApiInteractor {
    suspend fun getVersion(stand: Stand): String
    suspend fun sendEmailTemporaryCode(stand: Stand): Any
    fun downloadApk(stand: Stand): Call<ResponseBody>
}