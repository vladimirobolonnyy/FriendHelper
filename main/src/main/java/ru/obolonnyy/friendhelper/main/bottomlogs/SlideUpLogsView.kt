package ru.obolonnyy.friendhelper.main.bottomlogs

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import ru.obolonnyy.friendhelper.main.R
import ru.obolonnyy.friendhelper.utilsandroid.ScopedFrameLayout

class SlideUpLogsView : ScopedFrameLayout, KoinComponent {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    val model: LogsViewModel by inject()
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: SlideUpLogsAdapter
    private lateinit var clear: View

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
//        initViews()
//        observeViewModel()
    }

    private fun observeViewModel() {
        launch { model.text.consumeEach(::renderMessages) }
    }

    private fun renderMessages(text: List<String>) {
        adapter.updateElements(text)
    }

    private fun initViews() {
        recycler = this.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this.context)
        adapter = SlideUpLogsAdapter()
        recycler.adapter = adapter
        clear = this.findViewById(R.id.clear)
        clear.setOnClickListener { model.clear() }
    }
}