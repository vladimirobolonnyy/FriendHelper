package ru.obolonnyy.friendhelper.main.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.obolonnyy.friendhelper.main.R
import ru.obolonnyy.friendhelper.utilsandroid.ScopedFragment

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class MainFragment : ScopedFragment() {

    val viewModel: MainViewModel by inject()

    private lateinit var recycler: androidx.recyclerview.widget.RecyclerView
    private lateinit var adapter: MainAdapter
    private lateinit var root: View
    private lateinit var swipe: androidx.swiperefreshlayout.widget.SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        observeViewModel()
    }

    private fun initViews(view: View) {
        root = view.findViewById(R.id.main)
        recycler = view.findViewById(R.id.recycler)
        recycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this.context)
        adapter = createAdapter()
        recycler.adapter = adapter
        swipe = view.findViewById(R.id.swiper)
        swipe.setOnRefreshListener { refreshItems() }
    }

    private fun observeViewModel() {
        launch { viewModel.viewChannel.consumeEach(::render) }
    }

    private fun render(items: List<StandState>) {
        adapter.updateItems(items)
    }

    private fun refreshItems() {
        swipe.isRefreshing = false
        viewModel.refresh()
    }

    private fun createAdapter() = MainAdapter(
        elements = mutableListOf(),
        onVersionClicked = viewModel::onVersionClicked,
        onStatusClicked = viewModel::onStatusClicked,
        onFileClicked = viewModel::onFileClicked
    )

    companion object {
        fun newInstance() = MainFragment()
    }
}