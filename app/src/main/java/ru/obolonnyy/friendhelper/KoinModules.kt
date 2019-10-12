package ru.obolonnyy.friendhelper

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.obolonnyy.friendhelper.api.ApiInteractor
import ru.obolonnyy.friendhelper.api.Stand
import ru.obolonnyy.friendhelper.main.main.MainModel
import ru.obolonnyy.friendhelper.main.main.MainViewModel
import ru.obolonnyy.friendhelper.network.ApiInteractorImpl
import ru.obolonnyy.friendhelper.network.ElementsGenerator.initElements
import ru.obolonnyy.friendhelper.utils.constants.KoinConstants.CONTAINER
import ru.obolonnyy.friendhelper.utils.constants.KoinConstants.PROVIDER
import ru.obolonnyy.friendhelper.utilsandroid.getAvailableDownloadsDir

val appModule = module {
    single(named(PROVIDER)) { "${BuildConfig.APPLICATION_ID}.provider" }
    single<FragmentManager> { (androidContext() as FragmentActivity).supportFragmentManager }
    single<MenuRouter> { MenuRouterImpl(supportFragmentManager = get()) }
}

val mainModule = module {
    single(named(CONTAINER)) { (R.id.container) }
    single { androidContext().getAvailableDownloadsDir() }

    single<List<Stand>> { initElements() }
    single { MainModel(interactor = get(), downloadsDir = get()) }
    viewModel { MainViewModel(mainModel = get(), elements = get()) }
}

val networkModule = module {
    single<ApiInteractor> { ApiInteractorImpl() }
}

val koinModules = listOf(appModule, mainModule, networkModule)