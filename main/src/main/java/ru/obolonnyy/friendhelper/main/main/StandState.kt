package ru.obolonnyy.friendhelper.main.main

import android.view.View
import ru.obolonnyy.friendhelper.main.R
import ru.obolonnyy.friendhelper.utils.local.StandI

enum class FileStatus {
    NOT_LOADED,
    LOADING,
    LOADED,
    ERROR
}

data class StandState(
    val position: Int,
    val standI: StandI,
    var version: String = "???",
    var versionProgressVisibility: Int = View.GONE,

    var status: String = "???",
    var statusProgressVisibility: Int = View.GONE,
    var statusColor: Int = R.color.green,

    var fileImageResource: Int = R.drawable.ic_download,
    var fileProgressVisibility: Int = View.GONE,
    var fileVisibility: Int = View.GONE
) {

    var fileStatus: FileStatus = FileStatus.NOT_LOADED

    fun changeFileState(status: FileStatus) {
        fileStatus = status
        when (fileStatus) {
            FileStatus.NOT_LOADED -> {
                fileVisibility = View.VISIBLE
                fileImageResource = R.drawable.ic_download
                fileProgressVisibility = View.GONE
            }
            FileStatus.LOADING -> {
                fileVisibility = View.INVISIBLE
                fileProgressVisibility = View.VISIBLE
            }
            FileStatus.LOADED -> {
                fileVisibility = View.VISIBLE
                fileImageResource = R.drawable.ic_downloaded
                fileProgressVisibility = View.GONE
            }
            FileStatus.ERROR -> {
                fileVisibility = View.VISIBLE
                fileImageResource = R.drawable.ic_error
                fileProgressVisibility = View.GONE
            }
        }
    }
}