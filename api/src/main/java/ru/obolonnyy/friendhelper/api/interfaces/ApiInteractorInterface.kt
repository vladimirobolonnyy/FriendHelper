package ru.obolonnyy.friendhelper.api.interfaces

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import ru.obolonnyy.friendhelper.utils.data.StandI
import ru.obolonnyy.friendhelper.utils.data.VersionDto

interface ApiInteractorInterface {

    suspend fun getVersion(stand: StandI): Deferred<VersionDto>
    suspend fun sendEmailTemporaryCode(stand: StandI): Deferred<Any>
    suspend fun downloadApk(stand: StandI): Deferred<Response<ResponseBody>>
}