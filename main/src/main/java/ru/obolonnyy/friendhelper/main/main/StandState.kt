package ru.obolonnyy.friendhelper.main.main

import android.view.View
import ru.obolonnyy.friendhelper.main.R
import ru.obolonnyy.friendhelper.api.Stand

sealed class FileStatus {
    object NotLoaded : FileStatus()
    data class Loading(val process: Int) : FileStatus()
    object Loaded : FileStatus()
    object Error : FileStatus()
}

data class StandState(
    val position: Int,
    val stand: Stand,
    var version: String = "???",
    var versionProgressVisibility: Int = View.GONE,

    var status: String = "???",
    var statusProgressVisibility: Int = View.GONE,
    var statusColor: Int = R.color.green,

    var fileImageResource: Int = R.drawable.ic_download,
    var fileProgressVisibility: Int = View.GONE,
    var fileVisibility: Int = View.GONE,
    var downloadProgress: Int? = 0
) {

    var fileStatus: FileStatus = FileStatus.NotLoaded

    val fileIsClickable: Boolean
        get() = (fileStatus is FileStatus.Loading).not()

    fun changeFileState(status: FileStatus) {
        fileStatus = status
        when (fileStatus) {
            FileStatus.NotLoaded -> {
                fileVisibility = View.VISIBLE
                fileImageResource = R.drawable.ic_download
                fileProgressVisibility = View.GONE
                downloadProgress = null
            }
            is FileStatus.Loading -> {
                fileVisibility = View.INVISIBLE
                fileProgressVisibility = View.VISIBLE
                downloadProgress = (status as FileStatus.Loading).process
            }
            FileStatus.Loaded -> {
                fileVisibility = View.VISIBLE
                fileImageResource = R.drawable.ic_downloaded
                fileProgressVisibility = View.GONE
                downloadProgress = null
            }
            FileStatus.Error -> {
                fileVisibility = View.VISIBLE
                fileImageResource = R.drawable.ic_error
                fileProgressVisibility = View.GONE
                downloadProgress = null
            }
        }
    }
}