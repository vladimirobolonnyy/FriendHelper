package ru.obolonnyy.friendhelper

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import ru.obolonnyy.friendhelper.protect.ServerApi
import ru.obolonnyy.friendhelper.protect.Stand

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class MainFragment : ScopedFragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var recycler: RecyclerView
    private lateinit var swiper: SwipeRefreshLayout
    private val elements = Stand.values()
    private val apis by lazy { initApis() }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        recycler = view.findViewById(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this.context)
        recycler.adapter = createAdapter()
        swiper = view.findViewById(R.id.swiper)
        swiper.setOnRefreshListener { refreshItems() }
    }

    private fun initApis(): Map<Stand, ServerApi> {
        return hashMapOf<Stand, ServerApi>().apply {
            elements.forEach {
                this[it] = Helper.createRetrofit(it)
            }
        }
    }

    private fun refreshItems() {
        recycler.adapter = createAdapter()
        swiper.isRefreshing = false
    }

    private fun createAdapter() = SimpleAdapter(elements, this.context!!, apis, coroutineContext)
}