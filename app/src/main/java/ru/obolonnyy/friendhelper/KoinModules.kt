package ru.obolonnyy.friendhelper

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.obolonnyy.friendhelper.api.ApiInteractor
import ru.obolonnyy.friendhelper.api.Stand
import ru.obolonnyy.friendhelper.main.MainModel
import ru.obolonnyy.friendhelper.main.MainViewModel
import ru.obolonnyy.friendhelper.network.ApiInteractorImpl
import ru.obolonnyy.friendhelper.network.ElementsGenerator.initElements
import ru.obolonnyy.friendhelper.utils.constants.KoinConstants
import ru.obolonnyy.friendhelper.utils.constants.KoinConstants.PROVIDER
import ru.obolonnyy.friendhelper.utilsandroid.getAvailableDownloadsDir

val appModule = module {
    single(named(PROVIDER)) { "${BuildConfig.APPLICATION_ID}.provider" }
    single<MenuRouter> { (activity: FragmentActivity) -> MenuRouterImpl(fm = activity.supportFragmentManager, container = get(named(KoinConstants.CONTAINER))) }
    single<@IdRes Int>(named(KoinConstants.CONTAINER)) { (R.id.container) }
    single<List<Stand>> { initElements() }
}

val mainModule = module {
    single { androidContext().getAvailableDownloadsDir() }
    single { MainModel(interactor = get(), downloadsDir = get()) }
    viewModel { MainViewModel(mainModel = get(), elements = get()) }
}

val networkModule = module {
    single<ApiInteractor> { ApiInteractorImpl() }
}

val koinModules = listOf(appModule, mainModule, networkModule)