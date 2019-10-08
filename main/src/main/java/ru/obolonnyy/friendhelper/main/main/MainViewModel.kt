package ru.obolonnyy.friendhelper.main.main

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.obolonnyy.friendhelper.main.R
import ru.obolonnyy.friendhelper.utils.data.MyResult
import ru.obolonnyy.friendhelper.utils.data.StandI

class MainViewModel(val mainModel: MainModel, val elements: List<StandI>) : ViewModel() {

//    val mainModel: MainModel by inject()
//    val elements: List<StandI>  by inject()

    lateinit var lifecycleOwner: LifecycleOwner

    private val _viewChannel = MutableLiveData<MainViewState>()
    fun viewChannel() = _viewChannel
    private var viewState = MainViewState()

    private val _viewEvents = MutableLiveData<Event<MainViewEvent>>()
    fun viewEvents() = _viewEvents

    init {
        val elementsState = mutableListOf<StandState>()
        elements.forEachIndexed { index, standI ->
            val state = StandState(index, standI)
            elementsState.add(state)
        }
        viewState = MainViewState(items = elementsState)
        _viewChannel.postValue(viewState)
        refresh()
    }

    fun refresh() {
        viewState.items?.forEach {
            onVersionClicked(it)
            onStatusClicked(it)
        }
    }

    fun onVersionClicked(state: StandState) {
        viewModelScope.launch {
            val pos = state.position
            viewState.items?.get(pos)!!.versionProgressVisibility = View.VISIBLE
            viewState.items?.get(pos)!!.version = ""
            _viewChannel.postValue(viewState)
            val result = mainModel.getStandVersion(state.standI)
            when (result) {
                is MyResult.Success -> {
                    viewState.items?.get(pos)!!.version = result.data
                    if (viewState.items?.get(pos)!!.fileStatus != FileStatus.Loading(0)) {
                        viewState.items?.get(pos)!!.fileVisibility = View.VISIBLE
                    }
                }
                is MyResult.Error -> viewState.items?.get(pos)!!.version = result.message
            }
            viewState.items?.get(pos)!!.versionProgressVisibility = View.GONE
            if (mainModel.fileExists(state)) {
                state.changeFileState(FileStatus.Loaded)
            }
            _viewChannel.postValue(viewState)
        }
    }

    fun onStatusClicked(state: StandState) {
        viewModelScope.launch {
            val pos = state.position
            viewState.items?.get(pos)!!.statusProgressVisibility = View.VISIBLE
            viewState.items?.get(pos)!!.status = ""
            _viewChannel.postValue(viewState)
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
            _viewChannel.postValue(viewState)
        }
    }

    fun onFileClicked(state: StandState) {
        when (state.fileStatus) {
            FileStatus.NotLoaded -> downloadFile(state)
            is FileStatus.Loading -> { /* no need to do anything*/
            }
            FileStatus.Loaded -> {
                _viewEvents.postValue(Event(MainViewEvent.OpenFile(mainModel.getApkFile(state))))
            }
            FileStatus.Error -> downloadFile(state)
        }
    }

    private fun downloadFile(state: StandState) {
        GlobalScope.launch(Dispatchers.IO) {
            val pos = state.position
            viewState.items?.get(pos)!!.changeFileState(FileStatus.Loading(0))
            _viewChannel.postValue(viewState)
            val channel = MutableLiveData<Int>()
            val result = mainModel.downloadFile(state, channel)
            when (result) {
                is MyResult.Success -> {
                    withContext(Dispatchers.Main) {
                        channel.observe(lifecycleOwner, Observer {
                            if (it <= 99) {
                                viewState.items?.get(pos)!!.changeFileState(FileStatus.Loading(it))
                                _viewChannel.postValue(viewState)
                            } else {
                                viewState.items?.get(pos)!!.changeFileState(FileStatus.Loaded)
                                _viewChannel.postValue(viewState)
                            }
                        })
                    }
                }
                is MyResult.Error -> {
                    viewState.items?.get(pos)!!.changeFileState(FileStatus.Error)
                    _viewChannel.postValue(viewState)
                }
            }
        }
    }
}