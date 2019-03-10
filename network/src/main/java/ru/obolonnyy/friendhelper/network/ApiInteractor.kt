package ru.obolonnyy.friendhelper.network

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import ru.obolonnyy.friendhelper.api.interfaces.ApiInteractorInterface
import ru.obolonnyy.friendhelper.network.RetrofitHelper.createRetrofits
import ru.obolonnyy.friendhelper.utils.local.StandI
import ru.obolonnyy.friendhelper.utils.local.VersionDto

class ApiInteractor : ApiInteractorInterface {

    private val apis: Map<StandI, ServerApi> = createRetrofits()

    override suspend fun getVersion(stand: StandI): Deferred<VersionDto> {
        return apis[stand]!!.getVersion()
    }

    override suspend fun sendEmailTemporaryCode(stand: StandI): Deferred<Any> {
        return apis[stand]!!.sendEmailTemporaryCode(stand.context)
    }

    override suspend fun downloadApk(stand: StandI): Deferred<Response<ResponseBody>> {
        return apis[stand]!!.downloadApk()
    }
}