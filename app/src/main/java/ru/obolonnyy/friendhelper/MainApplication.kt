package ru.obolonnyy.friendhelper

import android.app.Application
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, koinModules)
        Timber.plant(Timber.DebugTree())
    }
}