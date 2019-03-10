package ru.obolonnyy.friendhelper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import ru.obolonnyy.friendhelper.main.bottomlogs.LogsViewModel
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class DataBaseLoggingTree : Timber.Tree(), CoroutineScope, KoinComponent {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    private val job by lazy { Job() }

    val model: LogsViewModel by inject()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        launch(Dispatchers.IO) {
            model.putSomeError(message)
        }
    }
}