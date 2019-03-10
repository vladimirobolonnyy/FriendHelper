package ru.obolonnyy.friendhelper.main.second

import ru.obolonnyy.friendhelper.utils.database.MainDataBase
import ru.obolonnyy.friendhelper.utils.database.MainElementInt
import ru.obolonnyy.friendhelper.utils.local.MainElementLocal
import timber.log.Timber

class SecondViewModel(val db: MainDataBase<MainElementInt>) {

    val getText: String = try {
        db.mainElementsDataBase().fetchOneMoviesbyMovieId(1).id ?: "none"
    } catch (e: Exception) {
        Timber.e(e)
        "error"
    }

    suspend fun onClick() {
        val obj = MainElementLocal("1", "1", "1", "1", "1", "1")
        db.mainElementsDataBase().insert(obj)
    }
}