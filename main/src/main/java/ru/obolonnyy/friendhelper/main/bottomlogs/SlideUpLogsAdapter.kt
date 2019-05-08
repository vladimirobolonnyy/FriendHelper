package ru.obolonnyy.friendhelper.main.bottomlogs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.obolonnyy.friendhelper.main.R

class SlideUpLogsAdapter(val elements: MutableList<String> = mutableListOf()) : RecyclerView.Adapter<SlideUpLogsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideUpLogsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_simple_text, parent, false)
        return SlideUpLogsViewHolder(view)
    }

    override fun getItemCount() = elements.size

    override fun onBindViewHolder(holder: SlideUpLogsViewHolder, position: Int) {
        holder.text.text = elements[position]
    }

    fun updateElements(newList: List<String>) {
        this.elements.clear()
        this.elements.addAll(newList)
        notifyDataSetChanged()
    }
}
