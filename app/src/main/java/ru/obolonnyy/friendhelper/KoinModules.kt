package ru.obolonnyy.friendhelper

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import ru.obolonnyy.friendhelper.api.interfaces.ApiInteractorInterface
import ru.obolonnyy.friendhelper.database.MyDataBase
import ru.obolonnyy.friendhelper.database.StringDataBase
import ru.obolonnyy.friendhelper.main.bottomlogs.LogsViewModel
import ru.obolonnyy.friendhelper.main.main.MainModel
import ru.obolonnyy.friendhelper.main.main.MainViewModel
import ru.obolonnyy.friendhelper.main.second.SecondViewModel
import ru.obolonnyy.friendhelper.network.ApiInteractor
import ru.obolonnyy.friendhelper.network.ElementsGenerator.initElements
import ru.obolonnyy.friendhelper.utils.constants.KoinConstants.PROVIDER
import ru.obolonnyy.friendhelper.utils.database.BaseDataBaseOperations
import ru.obolonnyy.friendhelper.utils.database.MainDataBase
import ru.obolonnyy.friendhelper.utils.database.MainElementInt
import ru.obolonnyy.friendhelper.utils.database.StringElementInt
import ru.obolonnyy.friendhelper.utils.local.StandI
import ru.obolonnyy.friendhelper.utilsandroid.getAvailableDownloadsDir
import java.io.File

val appModule = module {
    single(PROVIDER) { "${BuildConfig.APPLICATION_ID}.provider" }
}

val mainModule = module {
    single<List<StandI>> { initElements() }
    single<File> { androidContext().getAvailableDownloadsDir() }
    single { MainModel(interactor = get(), filesDir = get()) }
    single { MainViewModel(get(), get()) }
}

val networkModule = module {
    single<ApiInteractorInterface> { ApiInteractor() }
}

val roomModule = module {
    single<MainDataBase<MainElementInt>> { MyDataBase(androidContext()) }
    single<BaseDataBaseOperations<StringElementInt>> { StringDataBase(androidContext()).logsHistory() }
    single { SecondViewModel(db = get()) }
    single { DataBaseLoggingTree() }
    single { LogsViewModel() }
}

val koinModules = listOf(appModule, mainModule, networkModule, roomModule)