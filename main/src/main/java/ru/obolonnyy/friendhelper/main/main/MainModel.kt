package ru.obolonnyy.friendhelper.main.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.Okio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import ru.obolonnyy.friendhelper.api.interfaces.ApiInteractorInterface
import ru.obolonnyy.friendhelper.utils.constants.Constants
import ru.obolonnyy.friendhelper.utils.constants.Constants.DATAPOWER
import ru.obolonnyy.friendhelper.utils.data.MyResult
import ru.obolonnyy.friendhelper.utils.data.StandI
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class MainModel(
    val interactor: ApiInteractorInterface,
    val filesDir: File
) : ViewModel() {

    private val getJustApkFileName = "friend.apk"

    suspend fun getStandVersion(state: StandI): MyResult<String> {
        return try {
            val version = interactor.getVersion(state)
            MyResult.Success(version)
        } catch (ex: Exception) {
            Timber.e(ex)
            MyResult.Error(ex, Constants.ERROR)
        }
    }

    suspend fun getStandStatus(stand: StandI): MyResult<String> {
        val res = try {
            interactor.sendEmailTemporaryCode(stand)
            MyResult.Success(Constants.ONLINE)
        } catch (ex: Exception) {
            Timber.e(ex)
            when (ex) {
                is HttpException -> {
                    val errorMessage = ex.response()?.errorBody()?.string()
                    when {
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
        return res
    }

    fun downloadFile(state: StandState, channel: Channel<Int>): MyResult<Any> {

        return try {
            val call = interactor.downloadApk(state.standI)
            call.enqueue(object : Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Timber.i("server contacted and has file")

                        viewModelScope.launch(IO) {
                            val writtenToDisk = writeResponseBodyToDisk(response.body()!!, state, channel)
                            Timber.i("file download was a success? $writtenToDisk")
                        }

                    } else {
                        Timber.i("server contact failed")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Timber.e(t)
                }
            })
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

    private fun saveApkToFile(response: ResponseBody, state: StandState): Boolean {
        val filePath = getApkPathFile(state)
        var sink: BufferedSink? = null
        try {
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val file = File("$filePath/$getJustApkFileName")
            file.createNewFile()
            sink = Okio.buffer(Okio.sink(file))
            sink.writeAll(response.source())
            return true;
        } catch (ex: Exception) {
            Timber.e(ex, "sink writing error")
            return false
        } finally {
            try {
                sink?.close()
            } catch (ex: IOException) {
                Timber.e(ex, "sink closing error")
            }
        }
    }

    private fun writeResponseBodyToDisk(body: ResponseBody, state: StandState, channel: Channel<Int>): Boolean {
        try {
            val filePath = getApkPathFile(state)
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val file = File("$filePath/$getJustApkFileName")
            file.createNewFile()
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)
                while (true) {
                    val read = inputStream!!.read(fileReader)
                    Timber.i("readed ${fileReader.size}")
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Timber.i("file download: $fileSizeDownloaded of $fileSize")
                    channel.offer((fileSizeDownloaded * 100 / fileSize).toInt())
                }
                outputStream.flush()
                return true
            } catch (e: IOException) {
                return false
            } finally {
                viewModelScope.launch { channel.send(100) }
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            return false
        }

    }

    private fun getApkPathFile(state: StandState): File {
        return File(filesDir.toString() + getApkPath(state) + "/")
    }

    private fun getApkPath(state: StandState): String {
        return "/friend/${state.standI.engName}/${state.version}/"
    }
}