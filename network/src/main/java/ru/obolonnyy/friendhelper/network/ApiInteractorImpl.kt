package ru.obolonnyy.friendhelper.network

import okhttp3.ResponseBody
import retrofit2.Call
import ru.obolonnyy.friendhelper.api.ApiInteractor
import ru.obolonnyy.friendhelper.network.RetrofitHelper.createRetrofits
import ru.obolonnyy.friendhelper.api.Stand
import ru.obolonnyy.priv.network.ServerApi

class ApiInteractorImpl : ApiInteractor {

    private val apis: Map<Stand, ServerApi> = createRetrofits()

    override suspend fun getVersion(stand: Stand): String {
        return apis[stand]!!.getVersion(stand.app).version!!
    }

    override suspend fun sendEmailTemporaryCode(stand: Stand): Any {
        return apis[stand]!!.sendEmailTemporaryCode(stand.context)
    }

    override fun downloadApk(stand: Stand): Call<ResponseBody> {
        return apis[stand]!!.downloadApk()
    }
}