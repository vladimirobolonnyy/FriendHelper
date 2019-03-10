package ru.obolonnyy.friendhelper.main.bottomlogs

import android.view.View
import android.widget.TextView
import ru.obolonnyy.friendhelper.main.R

class SlideUpLogsViewHolder(root: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(root) {
    val text = root.findViewById<TextView>(R.id.name)!!
}