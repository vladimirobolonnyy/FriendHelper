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

    private var fileStatus: FileStatus = FileStatus.NOT_LOADED

    fun changeFileState(status: FileStatus) {
        fileStatus = status
        when (fileStatus) {
            FileStatus.NOT_LOADED -> {
                fileVisibility = View.VISIBLE
                fileImageResource = R.drawable.ic_download
                fileProgressVisibility = View.GONE
            }
            FileStatus.LOADING -> {
                fileVisibility = View.GONE
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StandState

        if (position != other.position) return false
        if (standI != other.standI) return false
        if (version != other.version) return false
        if (versionProgressVisibility != other.versionProgressVisibility) return false
        if (status != other.status) return false
        if (statusProgressVisibility != other.statusProgressVisibility) return false
        if (statusColor != other.statusColor) return false
        if (fileImageResource != other.fileImageResource) return false
        if (fileProgressVisibility != other.fileProgressVisibility) return false
        if (fileVisibility != other.fileVisibility) return false
        if (fileStatus != other.fileStatus) return false

        return true
    }

    override fun hashCode(): Int {
        var result = position
        result = 31 * result + standI.hashCode()
        result = 31 * result + version.hashCode()
        result = 31 * result + versionProgressVisibility
        result = 31 * result + status.hashCode()
        result = 31 * result + statusProgressVisibility
        result = 31 * result + statusColor
        result = 31 * result + fileImageResource
        result = 31 * result + fileProgressVisibility
        result = 31 * result + fileVisibility
        result = 31 * result + fileStatus.hashCode()
        return result
    }
}