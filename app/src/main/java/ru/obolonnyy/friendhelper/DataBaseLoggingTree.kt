package ru.obolonnyy.friendhelper

import org.koin.standalone.KoinComponent
import timber.log.Timber

class DataBaseLoggingTree() : Timber.Tree(), KoinComponent {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, tag, message, t)
    }
}