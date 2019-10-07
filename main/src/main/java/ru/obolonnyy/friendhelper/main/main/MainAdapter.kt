package ru.obolonnyy.friendhelper.main.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import ru.obolonnyy.friendhelper.utilsandroid.getColorCompat


class MainAdapter(
    val onVersionClicked: (StandState) -> (Unit),
    val onStatusClicked: (StandState) -> (Unit),
    val onFileClicked: (StandState) -> (Unit)
) : Adapter<MainViewHolder>() {

    private var elements: MutableList<StandState> = mutableListOf()

    override fun getItemCount() = elements.size

    override fun onCreateViewHolder(group: ViewGroup, position: Int): MainViewHolder {
        val view = LayoutInflater.from(group.context)
            .inflate(ru.obolonnyy.friendhelper.main.R.layout.item_stand, group, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        bindViewHolder(holder, elements[position])
    }

    private fun bindViewHolder(holder: MainViewHolder, elem: StandState) = with(holder) {
        name.text = elem.standI.stringName

        version.text = elem.version
        version.setOnClickListener { onVersionClicked(elem) }
        version.isClickable = elem.versionProgressVisibility == View.GONE
        versionProgress.visibility = elem.versionProgressVisibility

        status.text = elem.status
        status.setTextColor(holder.itemView.context.getColorCompat(elem.statusColor))
        status.setOnClickListener { onStatusClicked(elem) }
        status.isClickable = elem.statusProgressVisibility == View.GONE
        statusProgress.visibility = elem.statusProgressVisibility

        file.visibility = elem.fileVisibility
        file.setImageResource(elem.fileImageResource)
        file.setOnClickListener { onFileClicked(elem) }
        file.isClickable = elem.fileProgressVisibility == View.GONE
        fileProgress.visibility = elem.fileProgressVisibility

        downloadProgress.text = if (elem.downloadProgress == null) "" else "${elem.downloadProgress}"
    }

    fun updateItems(newItems: List<StandState>) {
        if (elements.isEmpty()) {
            elements.addAll(newItems)
        }
        notifyDataSetChanged()
    }
/*
    class StructureDiffCallback(
        private val oldList: List<StandState>,
        private val newList: List<StandState>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return true // because we have the same element all the time
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }*/
}
