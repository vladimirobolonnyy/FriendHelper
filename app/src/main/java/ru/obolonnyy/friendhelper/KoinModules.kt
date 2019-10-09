package ru.obolonnyy.friendhelper

import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.obolonnyy.friendhelper.api.ApiInteractor
import ru.obolonnyy.friendhelper.main.main.MainModel
import ru.obolonnyy.friendhelper.main.main.MainViewModel
import ru.obolonnyy.friendhelper.network.ApiInteractorImpl
import ru.obolonnyy.friendhelper.network.ElementsGenerator.initElements
import ru.obolonnyy.friendhelper.utils.constants.KoinConstants.CONTAINER
import ru.obolonnyy.friendhelper.utils.constants.KoinConstants.PROVIDER
import ru.obolonnyy.friendhelper.utils.data.StandI
import ru.obolonnyy.friendhelper.utilsandroid.getAvailableDownloadsDir

val appModule = module {
    single(named(PROVIDER)) { "${BuildConfig.APPLICATION_ID}.provider" }
}

val mainModule = module {
    single<List<StandI>> { initElements() }
    single { androidContext().getAvailableDownloadsDir() }
    single { MainModel(interactor = get(), downloadsDir = get()) }
    single(named(CONTAINER)) { (R.id.container) }
    viewModel { MainViewModel(get(), get()) }
}

val networkModule = module {
    single<ApiInteractor> { ApiInteractorImpl() }
}

val koinModules = listOf(appModule, mainModule, networkModule)