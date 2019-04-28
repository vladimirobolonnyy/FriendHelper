package ru.obolonnyy.friendhelper.main.main

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.Okio
import retrofit2.HttpException
import retrofit2.Response
import ru.obolonnyy.friendhelper.api.interfaces.ApiInteractorInterface
import ru.obolonnyy.friendhelper.utils.constants.Constants
import ru.obolonnyy.friendhelper.utils.constants.Constants.DATAPOWER
import ru.obolonnyy.friendhelper.utils.data.MyResult
import ru.obolonnyy.friendhelper.utils.local.StandI
import timber.log.Timber
import java.io.File
import java.io.IOException
import kotlin.coroutines.CoroutineContext

class MainModel(
    val interactor: ApiInteractorInterface,
    val filesDir: File
) : CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val getJustApkFileName = "friend.apk"

    suspend fun getStandVersion(state: StandI): MyResult<String> {
        return try {
            val response = interactor.getVersion(state).await()
            MyResult.Success(response.version!!)
        } catch (ex: Exception) {
            Timber.e(ex)
            MyResult.Error(ex, Constants.ERROR)
        }
    }

    suspend fun getStandStatus(stand: StandI): MyResult<String> {
        return try {
            interactor.sendEmailTemporaryCode(stand).await()
            MyResult.Success(Constants.ONLINE)
        } catch (ex: Exception) {
            Timber.e(ex)
            when (ex) {
                is HttpException -> {
                    val errorMessage = ex.response().errorBody()?.string()
                    return when {
                        errorMessage?.contains("ERROR_ID_NOTFOUND") == true -> MyResult.Success(Constants.ONLINE)
                        errorMessage?.contains("<!DOCTYPE html>") == true -> MyResult.Error(
                            ex,
                            Constants.SERVER_REINSTALLING
                        )
                        errorMessage?.contains(DATAPOWER) == true -> MyResult.Error(ex, Constants.DATA_POWER_ERROR)
                        else -> MyResult.Error(ex, Constants.SERVER_ERROR + " ${ex.code()}")
                    }
                }
                else -> MyResult.Error(ex, Constants.NOT_HTTP_ERROR)
            }
        }
    }

    suspend fun downloadFile(state: StandState): MyResult<Any> {
        return try {
            val response = interactor.downloadApk(state.standI).await()
            saveApkToFile(response, state)
            MyResult.Success("")
        } catch (ex: Exception) {
            Timber.e(ex)
            MyResult.Error(ex, ex.localizedMessage)
        }
    }

    fun fileExists(state: StandState): Boolean {
        return getApkFile(state).exists()
    }

    fun getApkFile(state: StandState): File {
        return File(filesDir.toString() + getApkPath(state) + getJustApkFileName)
    }

    private fun saveApkToFile(response: Response<ResponseBody>, state: StandState) {
        val filePath = getApkPathFile(state)
        var sink: BufferedSink? = null
        try {
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val file = File("$filePath/$getJustApkFileName")
            file.createNewFile()
            sink = Okio.buffer(Okio.sink(file))
            sink.writeAll(response.body()!!.source())
        } catch (ex: Exception) {
            Timber.e(ex, "sink writing error")
        } finally {
            try {
                sink?.close()
            } catch (ex: IOException) {
                Timber.e(ex, "sink closing error")
            }
        }
    }

    private fun getApkPathFile(state: StandState): File {
        return File(filesDir.toString() + getApkPath(state) + "/")
    }

    private fun getApkPath(state: StandState): String {
        return "/friend/${state.standI.engName}/${state.version}/"
    }
}