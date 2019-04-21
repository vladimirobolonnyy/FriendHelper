package ru.obolonnyy.friendhelper.database

import android.content.Context
import androidx.room.Room
import ru.obolonnyy.friendhelper.database.database.MainElementsDataBase
import ru.obolonnyy.friendhelper.database.datamodel.StandEntityImpl
import ru.obolonnyy.friendhelper.utils.data.StandEntityInt
import ru.obolonnyy.friendhelper.utils.database.MainDataBase
import ru.obolonnyy.friendhelper.utils.database.StandDataBaseOperations
import ru.obolonnyy.friendhelper.utils.getStringTimeStampWithDate
import java.util.Date

class StandDataBase(context: Context) : MainDataBase<StandEntityInt> {

    private val DATABASE_NAME = "main_db"
    private val db = Room.databaseBuilder(context, MainElementsDataBase::class.java, DATABASE_NAME)
        .build()

    override fun standDataBase(): StandDataBaseOperations<StandEntityInt> {
        return object : StandDataBaseOperations<StandEntityInt> {

            override fun insert(elem: StandEntityInt) {
                db.mainElementsDataBase().insert(elem.toEntity())
            }

            override fun insertAll(elem: List<StandEntityInt>) {
                elem.forEach { insert(it) }
            }

            override fun getAll(): List<StandEntityInt> {
                return db.mainElementsDataBase().getAll()
            }

            override fun getById(id: Int): StandEntityInt {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun clear() {
                db.mainElementsDataBase()
            }
        }
    }

    private fun StandEntityInt.toEntity(): StandEntityImpl {
        val elem = this
        return StandEntityImpl().apply {
            timeStamp = Date().getStringTimeStampWithDate()
            stringName = elem.stringName
            version = elem.version
            status = elem.status
        }
    }
}