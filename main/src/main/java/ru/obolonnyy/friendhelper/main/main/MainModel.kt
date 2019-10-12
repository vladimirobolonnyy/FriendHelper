package ru.obolonnyy.friendhelper.main.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import ru.obolonnyy.friendhelper.api.ApiInteractor
import ru.obolonnyy.friendhelper.utils.constants.Constants
import ru.obolonnyy.friendhelper.utils.constants.Constants.DATAPOWER
import ru.obolonnyy.friendhelper.utils.data.MyResult
import ru.obolonnyy.friendhelper.api.Stand
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class MainModel(
    val interactor: ApiInteractor,
    val downloadsDir: File
) : ViewModel() {

    init {
        Timber.i("## downloadsDir:= $downloadsDir")
    }

    private val getJustApkFileName = "friend.apk"

    suspend fun getStandVersion(state: Stand): MyResult<String> {
        return try {
            val version = interactor.getVersion(state)
            MyResult.Success(version)
        } catch (ex: Exception) {
            Timber.e(ex)
            MyResult.Error(message = Constants.ERROR)
        }
    }

    suspend fun getStandStatus(stand: Stand): MyResult<String> {
        return try {
            interactor.sendEmailTemporaryCode(stand)
            MyResult.Success(Constants.ONLINE)
        } catch (ex: HttpException) {
            val errorMessage = ex.response()?.errorBody()?.string()!!
            when {
                errorMessage.contains("ERROR_ID_NOTFOUND") -> MyResult.Success(Constants.ONLINE)
                errorMessage.contains("<!DOCTYPE html>") -> MyResult.Error(message = Constants.SERVER_REINSTALLING)
                errorMessage.contains(DATAPOWER) -> MyResult.Error(message = Constants.DATA_POWER_ERROR)
                else -> MyResult.Error(message = Constants.SERVER_ERROR + " ${ex.code()}")
            }
        } catch (ex: Exception) {
            MyResult.Error(message = Constants.NOT_HTTP_ERROR)
        }
    }

    fun downloadFile(state: StandState): MyResult<LiveData<Int>> {
        return try {
            val liveData = MutableLiveData<Int>()
            val call: Call<ResponseBody> = interactor.downloadApk(state.stand)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        viewModelScope.launch(IO) {
                            Timber.i("server contacted and has file")
                            val writtenToDisk = writeResponseBodyToDisk(response.body()!!, state, liveData)
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
            MyResult.Success(liveData)
        } catch (ex: Exception) {
            Timber.e(ex)
            MyResult.Error(ex)
        }
    }


    fun fileExists(state: StandState): Boolean {
        return getApkFile(state).exists()
    }

    fun getApkFile(state: StandState): File {
        return File(downloadsDir.toString() + getApkPath(state) + getJustApkFileName)
    }

    private fun writeResponseBodyToDisk(body: ResponseBody, state: StandState, liveData: MutableLiveData<Int>): Boolean {
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
                val fileReader = ByteArray(8192)
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
                    liveData.postValue((fileSizeDownloaded * 100 / fileSize).toInt())
                }
                outputStream.flush()
                return true
            } catch (e: IOException) {
                return false
            } finally {
                liveData.postValue(100)
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            return false
        }

    }

    private fun getApkPathFile(state: StandState): File {
        return File(downloadsDir.toString() + getApkPath(state) + "/")
    }

    private fun getApkPath(state: StandState): String {
        return "/friend/${state.stand.engName}/${state.version}/"
    }
}