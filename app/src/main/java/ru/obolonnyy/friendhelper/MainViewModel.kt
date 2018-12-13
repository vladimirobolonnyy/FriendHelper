package ru.obolonnyy.friendhelper

import android.arch.lifecycle.ViewModel
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.Okio
import retrofit2.HttpException
import retrofit2.Response
import ru.obolonnyy.friendhelper.protect.ServerApi
import ru.obolonnyy.friendhelper.protect.Stand
import ru.obolonnyy.friendhelper.utils.BaseBean1
import ru.obolonnyy.friendhelper.utils.Constants.DATA_POWER_ERROR
import ru.obolonnyy.friendhelper.utils.Constants.ERROR
import ru.obolonnyy.friendhelper.utils.Constants.ONLINE
import ru.obolonnyy.friendhelper.utils.Constants.SUCCESS
import ru.obolonnyy.friendhelper.utils.Constants.UNKNOWN_ERROR
import ru.obolonnyy.friendhelper.utils.MyResult
import ru.obolonnyy.friendhelper.utils.parseAsAuthService
import ru.obolonnyy.friendhelper.utils.toLocal
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.io.InterruptedIOException

class MainViewModel : ViewModel() {

    lateinit var apis: Map<Stand, ServerApi>
    lateinit var filesDir: File
    private val versions = HashMap<String, String>()

    suspend fun getStandVersion(stand: Stand): String {
        val result = getVersion(stand)
        return when (result) {
            is MyResult.Success -> result.data
            is MyResult.Error -> result.message
        }
    }

    private suspend fun getVersion(stand: Stand): MyResult<String> {
        return try {
            val response = apis[stand]!!.getVersion().await()
            versions[stand.engName] = response.toLocal().version
            MyResult.Success(response.toLocal().version)
        } catch (ex: Exception) {
            Timber.e(ex)
            MyResult.Error(ex, ERROR)
        }
    }

    suspend fun standAvailable(stand: Stand): String {
        val result = getStand(stand)
        return when (result) {
            is MyResult.Success -> ONLINE
            is MyResult.Error -> result.message
        }
    }

    private suspend fun getStand(stand: Stand): MyResult<String> {
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

    suspend fun downloadApk(stand: Stand): String {
        val result = downloadApkFromStand(stand)
        return when (result) {
            is MyResult.Success -> result.data
            is MyResult.Error -> result.message
        }
    }

    private suspend fun downloadApkFromStand(stand: Stand): MyResult<String> {
        return try {
            val response = apis[stand]!!.downloadApk().await()
            saveApkToFile(response, getApkFileName(stand))
            MyResult.Success(SUCCESS)
        } catch (ex: Exception) {
            Timber.e(ex)
            MyResult.Error(ex, ERROR)
        }
    }

    private fun saveApkToFile(response: Response<ResponseBody>, fileName: String) {
        var sink: BufferedSink? = null
        try {
            if (!filesDir.exists()) {
                filesDir.mkdir()
            }
            val file = File(filesDir.toString() + fileName)
            if (!file.exists()) {
                file.mkdir()
            }
            sink = Okio.buffer(Okio.sink(file))
            sink!!.writeAll(response.body()!!.source())
        } catch (e: Exception) {
        } finally {
            try {
                sink?.close()
            } catch (e: IOException) {
                if (e !is InterruptedIOException) {
                    Timber.e(e, "sink closing error")
                }
            }

        }
    }

    fun getApkFile(stand: Stand): File {
        return File(filesDir.toString() + getApkFileName(stand))
    }

    fun getApkFileName(stand: Stand): String {
        //ToDo add version
        val version = versions[stand.engName]
        return "/${stand.engName}/$version/friend.apk"
    }
}