package ru.obolonnyy.friendhelper.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.TextView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import ru.obolonnyy.friendhelper.utils.constants.KoinConstants.PROVIDER
import java.io.File

class MainFragment : Fragment() {

    val mainViewModel: MainViewModel by viewModel()

    val provider: String by inject(named(PROVIDER))

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
        observeViewModel()
        initViews(view)
    }

    private fun initViews(view: View) {
        root = view.findViewById(R.id.main)
        recycler = view.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this.context)
        adapter = createAdapter()
        recycler.adapter = adapter
        swipe = view.findViewById(R.id.swiper)
        swipe.setOnRefreshListener { refreshItems() }
        view.findViewById<TextView>(R.id.version).text = "Version: ${BuildConfig.VERSION_NAME}"

    }

    private fun observeViewModel() {
        mainViewModel.lifecycleOwner = this
        mainViewModel.viewChannel().observe(this, Observer { render(it) })
        mainViewModel.viewEvents().observe(this, Observer { renderEvents(it) })
    }

    private fun renderEvents(event: Event<MainViewEvent>) {
        when (val content = event.getContentIfNotHandled()) {
            is MainViewEvent.OpenFile -> openFolder(content.file)
            else -> {/*nothing*/
            }
        }
    }

    private fun render(state: MainViewState) {
        with(state) {
            items?.let { adapter.updateItems(it) }
        }
    }

    private fun refreshItems() {
        swipe.isRefreshing = false
        mainViewModel.refresh()
    }

    private fun createAdapter() = MainAdapter(
        onVersionClicked = mainViewModel::onVersionClicked,
        onStatusClicked = mainViewModel::onStatusClicked,
        onFileClicked = mainViewModel::onFileClicked
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