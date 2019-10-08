package ru.obolonnyy.friendhelper

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            printLogger()
            androidContext(this@MainApplication)
            modules(koinModules)
        }
        Timber.plant(Timber.DebugTree())
    }
}