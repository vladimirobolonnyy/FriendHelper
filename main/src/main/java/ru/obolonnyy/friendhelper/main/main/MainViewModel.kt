package ru.obolonnyy.friendhelper.main.main

import android.view.View
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import ru.obolonnyy.friendhelper.main.R
import ru.obolonnyy.friendhelper.utils.data.MyResult
import ru.obolonnyy.friendhelper.utils.local.StandI
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class MainViewModel(
    private val mainModel: MainModel,
    elements: List<StandI>
) : CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val viewChannel = ConflatedBroadcastChannel<List<StandState>>()
    private var viewState = emptyList<StandState>()

    private val getJustApkFileName = "friend.apk"

    init {
        val elementsState = mutableListOf<StandState>()
        elements.forEachIndexed { index, standI ->
            val state = StandState(index, standI)
            elementsState.add(state)
        }
        viewState = elementsState
        viewChannel.offer(viewState)
    }

    fun refresh() {

    }

    fun onVersionClicked(state: StandState) {
        launch {
            val pos = state.position
            viewState[pos].versionProgressVisibility = View.VISIBLE
            viewState[pos].version = ""
            viewChannel.offer(viewState)
            val result = mainModel.getStandVersion(state.standI)
            when (result) {
                is MyResult.Success -> viewState[pos].version = result.data
                is MyResult.Error -> viewState[pos].version = result.message
            }
            viewState[pos].versionProgressVisibility = View.GONE
            viewChannel.offer(viewState)
        }
    }

    fun onStatusClicked(state: StandState) {
        launch {
            val pos = state.position
            viewState[pos].statusProgressVisibility = View.VISIBLE
            viewState[pos].status = ""
            viewChannel.offer(viewState)
            val result = mainModel.getStandStatus(state.standI)
            when (result) {
                is MyResult.Success -> {
                    viewState[pos].status = result.data
                    viewState[pos].statusColor = R.color.green
                }
                is MyResult.Error -> {
                    viewState[pos].status = result.message
                    viewState[pos].statusColor = R.color.red
                }
            }
            viewState[pos].statusProgressVisibility = View.GONE
            viewChannel.offer(viewState)
        }
    }

    fun onFileClicked(state: StandState) {
//        launch {
//            val pos = state.position
////            viewState[pos].versionProgressVisibility = View.VISIBLE
////            viewState[pos].version = ""
//            viewChannel.offer(viewState)
//            val result = mainModel.getStandVersion(state.standI)
//            when (result) {
//                is MyResult.Success -> viewState[pos].version = result.data
//                is MyResult.Error -> viewState[pos].version = result.message
//            }
////            viewState[pos].versionProgressVisibility = View.GONE
////            viewChannel.offer(viewState)
//        }
    }
//
//
//    suspend fun downloadApk(stand: StandI, remoteVersion: String): String {
//        val result = downloadApkFromStand(stand, remoteVersion)
//        return when (result) {
//            is MyResult.Success -> result.data
//            is MyResult.Error -> result.message
//        }
//    }
//
//    private suspend fun downloadApkFromStand(stand: StandI, remoteVersion: String): MyResult<String> {
//        return try {
//            val response = interactor.downloadApk(stand).await()
//            saveApkToFile(response, stand, remoteVersion)
//            MyResult.Success(SUCCESS)
//        } catch (ex: Exception) {
//            Timber.e(ex)
//            MyResult.Error(ex, ERROR)
//        }
//    }
//
//    private fun saveApkToFile(response: Response<ResponseBody>, stand: StandI, remoteVersion: String) {
//        val filePath = getApkPathFile(stand, remoteVersion)
//        var sink: BufferedSink? = null
//        try {
//            if (!filePath.exists()) {
//                filePath.mkdirs()
//            }
//            val file = File("$filePath/$getJustApkFileName")
//            file.createNewFile()
//            sink = Okio.buffer(Okio.sink(file))
//            sink.writeAll(response.body()!!.source())
//        } catch (ex: Exception) {
//            Timber.e(ex, "sink writing error")
//        } finally {
//            try {
//                sink?.close()
//            } catch (ex: IOException) {
//                Timber.e(ex, "sink closing error")
//            }
//        }
//    }
//
//    private fun getApkPathFile(elem: StandI, version: String): File {
//        return File(filesDir.toString() + getApkPath(elem, version) + "/")
//    }
//
//    fun getApkFile(elem: StandI, version: String): File {
//        return File(filesDir.toString() + getApkPath(elem, version) + getJustApkFileName)
//    }
//
//    fun fileExists(elem: StandI, version: String): Boolean {
//        return getApkFile(elem, version).exists()
//    }
//
//    private fun getApkPath(elem: StandI, version: String): String {
//        return "/friend/${elem.engName}/$version/"
//    }
}