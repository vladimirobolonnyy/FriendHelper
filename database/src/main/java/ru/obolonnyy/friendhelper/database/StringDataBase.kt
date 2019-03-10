package ru.obolonnyy.friendhelper.database

import android.content.Context
import androidx.room.Room
import ru.obolonnyy.friendhelper.database.database.MainElementsDataBase
import ru.obolonnyy.friendhelper.database.datamodel.StringElement

class StringDataBase(context: Context) {

    fun logsHistory(): ru.obolonnyy.friendhelper.utils.database.BaseDataBaseOperations<ru.obolonnyy.friendhelper.utils.database.StringElementInt> {
        return object :
            ru.obolonnyy.friendhelper.utils.database.BaseDataBaseOperations<ru.obolonnyy.friendhelper.utils.database.StringElementInt> {
            override fun clear() {
                return db.logsHistory().deleteAll()
            }

            override fun getAll(): List<ru.obolonnyy.friendhelper.utils.database.StringElementInt> {
                return db.logsHistory().getAll()
            }

            override fun insert(elem: ru.obolonnyy.friendhelper.utils.database.StringElementInt) {
                val el = StringElement().apply {
                    id = elem.id
                    name = elem.name
                }
                db.logsHistory().insert(el)
            }

            override fun insertAll(elem: List<ru.obolonnyy.friendhelper.utils.database.StringElementInt>) {
                throw UnsupportedOperationException("Do no use it")
            }

            override fun fetchOneMoviesbyMovieId(elemId: Int): ru.obolonnyy.friendhelper.utils.database.StringElementInt {
                throw UnsupportedOperationException("Do no use it")
            }
        }
    }

    private val DATABASE_NAME = "main_db"
    val db = Room.databaseBuilder(context, MainElementsDataBase::class.java, DATABASE_NAME)
        .build()
}