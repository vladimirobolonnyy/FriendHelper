package ru.obolonnyy.friendhelper

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import ru.obolonnyy.friendhelper.api.interfaces.ApiInteractorInterface
import ru.obolonnyy.friendhelper.database.StandDataBase
import ru.obolonnyy.friendhelper.main.bottomlogs.LogsViewModel
import ru.obolonnyy.friendhelper.main.main.MainModel
import ru.obolonnyy.friendhelper.main.main.MainViewModel
import ru.obolonnyy.friendhelper.network.ApiInteractor
import ru.obolonnyy.friendhelper.network.ElementsGenerator.initElements
import ru.obolonnyy.friendhelper.utils.constants.KoinConstants.PROVIDER
import ru.obolonnyy.friendhelper.utilsandroid.getAvailableDownloadsDir

val appModule = module {
    single(PROVIDER) { "${BuildConfig.APPLICATION_ID}.provider" }
}

val mainModule = module {
//    single<List<StandI>> { initElements() }
//    single<File> { androidContext().getAvailableDownloadsDir() }
    single { initElements() }
    single { androidContext().getAvailableDownloadsDir() }
    single { MainModel(interactor = get(), filesDir = get(), db = get()) }
    single { MainViewModel(get(), get()) }
}

val networkModule = module {
    single<ApiInteractorInterface> { ApiInteractor() }
}

val roomModule = module {
    //    single <StandDataBaseOperations<StandEntityInt>> { StandDataBase(androidContext()).standDataBase() }
    single { StandDataBase(androidContext()).standDataBase() }
    single { DataBaseLoggingTree() }
    single { LogsViewModel() }
}

val koinModules = listOf(appModule, mainModule, networkModule, roomModule)