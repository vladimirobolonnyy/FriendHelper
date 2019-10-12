package ru.obolonnyy.friendhelper.main

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

class MainViewHolder(root: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(root) {
    val name = root.findViewById<TextView>(R.id.name)!!
    val version = root.findViewById<TextView>(R.id.version)!!
    val versionProgress = root.findViewById<ProgressBar>(R.id.progress_version)!!
    val status = root.findViewById<TextView>(R.id.status)!!
    val statusProgress = root.findViewById<ProgressBar>(R.id.progress_status)!!
    val file = root.findViewById<ImageView>(R.id.download)!!
    val downloadProgress = root.findViewById<TextView>(R.id.download_progress)!!
}