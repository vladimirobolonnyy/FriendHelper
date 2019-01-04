package ru.obolonnyy.friendhelper

import android.arch.lifecycle.ViewModel
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.Okio
import retrofit2.HttpException
import retrofit2.Response
import ru.obolonnyy.friendhelper.protect.ServerApi
import ru.obolonnyy.friendhelper.protect.Stand
import ru.obolonnyy.friendhelper.utils.*
import ru.obolonnyy.friendhelper.utils.Constants.DATA_POWER_ERROR
import ru.obolonnyy.friendhelper.utils.Constants.ERROR
import ru.obolonnyy.friendhelper.utils.Constants.ONLINE
import ru.obolonnyy.friendhelper.utils.Constants.SUCCESS
import ru.obolonnyy.friendhelper.utils.Constants.UNKNOWN_ERROR
import timber.log.Timber
import java.io.File
import java.io.IOException

class MainViewModel : ViewModel() {

    lateinit var apis: Map<StandI, ServerApi>
    lateinit var filesDir: File
    private val getJustApkFileName = "friend.apk"

    suspend fun getStandVersion(stand: StandI): String {
        val result = getVersion(stand)
        return when (result) {
            is MyResult.Success -> result.data
            is MyResult.Error -> result.message
        }
    }

    private suspend fun getVersion(stand: StandI): MyResult<String> {
        return try {
            val response = apis[stand]!!.getVersion().await()
            MyResult.Success(response.toLocal().version)
        } catch (ex: Exception) {
            Timber.e(ex)
            MyResult.Error(ex, ERROR)
        }
    }

    suspend fun standAvailable(stand: StandI): TextColor {
        val result = getStand(stand)
        return when (result) {
            is MyResult.Success -> TextColor(ONLINE, "GREEN")
            is MyResult.Error -> TextColor(result.message, "RED")
        }
    }

    private suspend fun getStand(stand: StandI): MyResult<String> {
        //ToDo вот с этим что-то надо сделать
        return try {
            when (stand) {
                Stand.DEV -> apis[stand]!!.authDev(BaseBean1()).await()
                else -> apis[stand]!!.auth(BaseBean1()).await()
            }
            MyResult.Success(ONLINE)
        } catch (ex: Exception) {
            Timber.e(ex)
            when (ex) {
                is HttpException -> {
                    val error = ex.parseAsAuthService()
                    if (error?.exceptionClass?.endsWith("AuthException") == true)
                        MyResult.Success(ONLINE)
                    else {
                        if (error?.errmsg?.contains("power") == true) {
                            MyResult.Error(ex, DATA_POWER_ERROR)
                        } else {
                            MyResult.Error(ex, UNKNOWN_ERROR)
                        }
                    }
                }
                else -> MyResult.Error(ex, UNKNOWN_ERROR)
            }
        }
    }

    suspend fun downloadApk(stand: StandI, remoteVersion: String): String {
        val result = downloadApkFromStand(stand, remoteVersion)
        return when (result) {
            is MyResult.Success -> result.data
            is MyResult.Error -> result.message
        }
    }

    private suspend fun downloadApkFromStand(stand: StandI, remoteVersion: String): MyResult<String> {
        return try {
            val response = apis[stand]!!.downloadApk().await()
            saveApkToFile(response, stand, remoteVersion)
            MyResult.Success(SUCCESS)
        } catch (ex: Exception) {
            Timber.e(ex)
            MyResult.Error(ex, ERROR)
        }
    }

    private fun saveApkToFile(response: Response<ResponseBody>, stand: StandI, remoteVersion: String) {
        val filePath = getApkPathFile(stand, remoteVersion)
        var sink: BufferedSink? = null
        try {
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val file = File(filePath.toString() + "/" + getJustApkFileName)
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

    private fun getApkPathFile(elem: StandI, version: String): File {
        return File(filesDir.toString() + getApkPath(elem, version) + "/")
    }

    fun getApkFile(elem: StandI, version: String): File {
        return File(filesDir.toString() + getApkPath(elem, version) + getJustApkFileName)
    }

    fun fileExists(elem: StandI, version: String): Boolean {
        return getApkFile(elem, version).exists()
    }

    private fun getApkPath(elem: StandI, version: String): String {
        return "/friend/${elem.engName}/$version/"
    }
}