package ru.obolonnyy.friendhelper.main.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.obolonnyy.friendhelper.main.R
import ru.obolonnyy.friendhelper.utils.constants.KoinConstants.PROVIDER
import ru.obolonnyy.friendhelper.utilsandroid.ScopedFragment
import java.io.File

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class MainFragment : ScopedFragment() {

    val viewModel: MainViewModel by inject()
    val provider: String by inject(PROVIDER)

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: MainAdapter
    private lateinit var root: View
    private lateinit var swipe: SwipeRefreshLayout

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
        recycler.layoutManager = LinearLayoutManager(this.context)
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

    private fun render(state: MainViewState) {
        with(state) {
            items?.let { adapter.updateItems(it) }
            file?.let { openFolder(it) }
        }
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

    private fun openFolder(file: File) {
        val mime = MimeTypeMap.getSingleton()
        val ext = file.name.substring(file.name.lastIndexOf(".") + 1)
        val type = mime.getMimeTypeFromExtension(ext)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        if (Build.VERSION.SDK_INT >= 24) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(context!!, provider, file)
            intent.setDataAndType(contentUri, type)
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
        startActivity(intent)
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}