package ru.obolonnyy.friendhelper.main.main

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch
import ru.obolonnyy.friendhelper.main.R
import ru.obolonnyy.friendhelper.utils.data.MyResult
import ru.obolonnyy.friendhelper.utils.data.StandI
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class MainViewModel(
    private val mainModel: MainModel,
    elements: List<StandI>
) : CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    val viewChannel = ConflatedBroadcastChannel<MainViewState>()
    private var viewState = MainViewState()

    init {
        val elementsState = mutableListOf<StandState>()
        elements.forEachIndexed { index, standI ->
            val state = StandState(index, standI)
            elementsState.add(state)
        }
        viewState = MainViewState(items = elementsState)
        viewChannel.offer(viewState)
        refresh()
    }

    fun refresh() {
        viewState.items?.forEach {
            onVersionClicked(it)
            onStatusClicked(it)
        }
    }

    fun onVersionClicked(state: StandState) {
        launch {
            val pos = state.position
            viewState.file = null
            viewState.items?.get(pos)!!.versionProgressVisibility = View.VISIBLE
            viewState.items?.get(pos)!!.version = ""
            viewChannel.offer(viewState)
            val result = mainModel.getStandVersion(state.standI)
            when (result) {
                is MyResult.Success -> {
                    viewState.items?.get(pos)!!.version = result.data
                    if (viewState.items?.get(pos)!!.fileStatus != FileStatus.LOADING) {
                        viewState.items?.get(pos)!!.fileVisibility = View.VISIBLE
                    }
                }
                is MyResult.Error -> viewState.items?.get(pos)!!.version = result.message
            }
            viewState.items?.get(pos)!!.versionProgressVisibility = View.GONE
            if (mainModel.fileExists(state)) {
                state.changeFileState(FileStatus.LOADED)
            }
            viewChannel.offer(viewState)
        }
    }

    fun onStatusClicked(state: StandState) {
        launch {
            val pos = state.position
            viewState.file = null
            viewState.items?.get(pos)!!.statusProgressVisibility = View.VISIBLE
            viewState.items?.get(pos)!!.status = ""
            viewChannel.offer(viewState)
            val result = mainModel.getStandStatus(state.standI)
            when (result) {
                is MyResult.Success -> {
                    viewState.items?.get(pos)!!.status = result.data
                    viewState.items?.get(pos)!!.statusColor = R.color.green
                }
                is MyResult.Error -> {
                    viewState.items?.get(pos)!!.status = result.message
                    viewState.items?.get(pos)!!.statusColor = R.color.red
                }
            }
            viewState.items?.get(pos)!!.statusProgressVisibility = View.GONE
            viewChannel.offer(viewState)
        }
    }

    fun onFileClicked(state: StandState) {
        when (state.fileStatus) {
            FileStatus.NOT_LOADED -> downloadFile(state)
            FileStatus.LOADING -> { /* no need to do anything*/
            }
            FileStatus.LOADED -> {
                viewState = viewState.copy(file = mainModel.getApkFile(state))
                viewChannel.offer(viewState)
            }
            FileStatus.ERROR -> downloadFile(state)
        }
    }

    private fun downloadFile(state: StandState) {
        GlobalScope.launch (Dispatchers.IO) {
            val pos = state.position
            viewState.items?.get(pos)!!.changeFileState(FileStatus.LOADING)
            viewChannel.offer(viewState)
            val result = mainModel.downloadFile(state)
            when (result) {
                is MyResult.Success -> viewState.items?.get(pos)!!.changeFileState(FileStatus.LOADED)
                is MyResult.Error -> viewState.items?.get(pos)!!.changeFileState(FileStatus.ERROR)
            }
            viewChannel.offer(viewState)
        }
    }
}