package ru.obolonnyy.friendhelper.network

import okhttp3.ResponseBody
import retrofit2.Call
import ru.obolonnyy.friendhelper.api.ApiInteractor
import ru.obolonnyy.friendhelper.network.RetrofitHelper.createRetrofits
import ru.obolonnyy.friendhelper.utils.data.StandI
import ru.obolonnyy.priv.network.ServerApi

class ApiInteractorImpl : ApiInteractor {

    private val apis: Map<StandI, ServerApi> = createRetrofits()

    override suspend fun getVersion(stand: StandI): String {
        return apis[stand]!!.getVersion().version!!
    }

    override suspend fun sendEmailTemporaryCode(stand: StandI): Any {
        return apis[stand]!!.sendEmailTemporaryCode(stand.context)
    }

    override fun downloadApk(stand: StandI): Call<ResponseBody> {
        return apis[stand]!!.downloadApk()
    }
}