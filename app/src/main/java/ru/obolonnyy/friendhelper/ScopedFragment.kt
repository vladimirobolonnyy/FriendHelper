package ru.obolonnyy.friendhelper

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class ScopedFragment: Fragment(), CoroutineScope {
    protected lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        job = Job()
    }

    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
    }

    fun View.disableWithTimeout(timeoutMs: Long = 200) {
        isEnabled = false
        launch {
            delay(timeoutMs)
            isEnabled = true
        }
    }
}