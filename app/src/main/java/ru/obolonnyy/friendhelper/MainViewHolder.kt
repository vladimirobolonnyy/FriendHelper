package ru.obolonnyy.friendhelper

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

class MainViewHolder(root: View) : RecyclerView.ViewHolder(root) {
    val name = root.findViewById<TextView>(R.id.name)!!
    val version = root.findViewById<TextView>(R.id.version)!!
    val version_progress = root.findViewById<ProgressBar>(R.id.progress_version)!!
    val status = root.findViewById<TextView>(R.id.status)!!
    val status_progress = root.findViewById<ProgressBar>(R.id.progress_status)!!
    val download = root.findViewById<ImageView>(R.id.download)!!
    val download_progress = root.findViewById<ProgressBar>(R.id.progress_download)!!
}