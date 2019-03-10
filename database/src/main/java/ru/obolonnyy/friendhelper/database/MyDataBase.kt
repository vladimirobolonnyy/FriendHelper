package ru.obolonnyy.friendhelper.database

import android.content.Context
import androidx.room.Room
import ru.obolonnyy.friendhelper.database.database.MainElementsDataBase
import ru.obolonnyy.friendhelper.database.datamodel.MainElement

class MyDataBase(context: Context) :
    ru.obolonnyy.friendhelper.utils.database.MainDataBase<ru.obolonnyy.friendhelper.utils.database.MainElementInt> {

    override fun logsHistory(): ru.obolonnyy.friendhelper.utils.database.BaseDataBaseOperations<ru.obolonnyy.friendhelper.utils.database.MainElementInt> {
        return object :
            ru.obolonnyy.friendhelper.utils.database.BaseDataBaseOperations<ru.obolonnyy.friendhelper.utils.database.MainElementInt> {
            override fun clear() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getAll(): List<ru.obolonnyy.friendhelper.utils.database.MainElementInt> {
                throw UnsupportedOperationException("Do no use it")
            }

            override fun insert(elem: ru.obolonnyy.friendhelper.utils.database.MainElementInt) {
                throw UnsupportedOperationException("Do no use it")

            }

            override fun insertAll(elem: List<ru.obolonnyy.friendhelper.utils.database.MainElementInt>) {
                throw UnsupportedOperationException("Do no use it")
            }

            override fun fetchOneMoviesbyMovieId(elemId: Int): ru.obolonnyy.friendhelper.utils.database.MainElementInt {
                throw UnsupportedOperationException("Do no use it")
            }
        }
    }

    override fun mainElementsDataBase(): ru.obolonnyy.friendhelper.utils.database.BaseDataBaseOperations<ru.obolonnyy.friendhelper.utils.database.MainElementInt> {
        return object :
            ru.obolonnyy.friendhelper.utils.database.BaseDataBaseOperations<ru.obolonnyy.friendhelper.utils.database.MainElementInt> {
            override fun clear() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getAll(): List<ru.obolonnyy.friendhelper.utils.database.MainElementInt> {
                return db.mainElementsDataBase().getAll()
            }

            override fun insert(elem: ru.obolonnyy.friendhelper.utils.database.MainElementInt) {
                val el = MainElement().apply {
                    id = elem.id
                }
                db.mainElementsDataBase().insert(el)
            }

            override fun insertAll(elem: List<ru.obolonnyy.friendhelper.utils.database.MainElementInt>) {
                val el = listOf<MainElement>()
                db.mainElementsDataBase().insertAll(el)
            }

            override fun fetchOneMoviesbyMovieId(elemId: Int): ru.obolonnyy.friendhelper.utils.database.MainElementInt {
                return db.mainElementsDataBase().fetchOneMoviesbyMovieId(elemId)
            }
        }
    }

    private val DATABASE_NAME = "main_db"
    val db = Room.databaseBuilder(context, MainElementsDataBase::class.java, DATABASE_NAME)
        .build()


}