package ru.obolonnyy.friendhelper.api.interfaces

import okhttp3.ResponseBody
import retrofit2.Response
import ru.obolonnyy.friendhelper.utils.data.StandI
import ru.obolonnyy.friendhelper.utils.data.VersionDto

interface ApiInteractorInterface {

    suspend fun getVersion(stand: StandI): VersionDto
    suspend fun sendEmailTemporaryCode(stand: StandI): Any
    suspend fun downloadApk(stand: StandI): Response<ResponseBody>
}