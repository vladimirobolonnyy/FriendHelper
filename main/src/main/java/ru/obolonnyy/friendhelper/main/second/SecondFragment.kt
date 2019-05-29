package ru.obolonnyy.friendhelper.main.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import ru.obolonnyy.friendhelper.main.R
import ru.obolonnyy.friendhelper.main.main.MainAdapter
import ru.obolonnyy.friendhelper.main.main.StandState
import ru.obolonnyy.friendhelper.utils.data.StandC
import ru.obolonnyy.friendhelper.utils.data.StandEntityInt
import ru.obolonnyy.friendhelper.utils.data.StandI
import ru.obolonnyy.friendhelper.utils.database.StandRepository
import ru.obolonnyy.friendhelper.utilsandroid.ScopedFragment

class SecondFragment : ScopedFragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: MainAdapter

    val repository: StandRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.second_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) = launch {
        val data = withContext(IO) { repository.getAllData() }
        val standStates = data
            .mapIndexed { index: Int, standEntityInt: StandEntityInt -> StandState(index, standEntityInt.toI()) }
        standStates.forEachIndexed { index: Int, standState: StandState ->
            standState.version = data[index].version!!
            standState.status = data[index].status!!
        }
        recycler = view.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(context)
        adapter = createAdapter(standStates.toMutableList())
        recycler.adapter = adapter
    }

    private fun createAdapter(list: MutableList<StandState>) = MainAdapter(
        elements = list,
        onVersionClicked = {},
        onStatusClicked = {},
        onFileClicked = {}
    )

    private fun StandEntityInt.toI(): StandI {
        return StandC(
            stringName = this.stringName,
            url = "",
            engName = this.stringName,
            context = ""
        )
    }
}