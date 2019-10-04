package ru.obolonnyy.friendhelper

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import ru.obolonnyy.friendhelper.api.ApiInteractor
import ru.obolonnyy.friendhelper.main.main.MainModel
import ru.obolonnyy.friendhelper.network.ApiInteractorImpl
import ru.obolonnyy.friendhelper.network.ElementsGenerator.initElements
import ru.obolonnyy.friendhelper.utils.constants.KoinConstants.CONTAINER
import ru.obolonnyy.friendhelper.utils.constants.KoinConstants.PROVIDER
import ru.obolonnyy.friendhelper.utilsandroid.getAvailableDownloadsDir

val appModule = module {
    single(PROVIDER) { "${BuildConfig.APPLICATION_ID}.provider" }
}

val mainModule = module {
    single { initElements() }
    single { androidContext().getAvailableDownloadsDir() }
    single { MainModel(interactor = get(), filesDir = get()) }
    single(CONTAINER) { (R.id.container) }
}

val networkModule = module {
    single<ApiInteractor> { ApiInteractorImpl() }
}

val koinModules = listOf(appModule, mainModule, networkModule)