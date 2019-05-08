package ru.obolonnyy.friendhelper

import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import ru.obolonnyy.friendhelper.main.bottomlogs.LogsViewModel
import timber.log.Timber

class DataBaseLoggingTree : Timber.Tree(), KoinComponent {
    val model: LogsViewModel by inject()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        model.putSomeLogs(message)
    }
}