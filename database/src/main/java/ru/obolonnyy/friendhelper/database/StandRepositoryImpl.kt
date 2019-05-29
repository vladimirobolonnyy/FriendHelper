package ru.obolonnyy.friendhelper.database

import ru.obolonnyy.friendhelper.database.datamodel.StandEntityImpl
import ru.obolonnyy.friendhelper.utils.data.StandEntityInt
import ru.obolonnyy.friendhelper.utils.data.StandI
import ru.obolonnyy.friendhelper.utils.database.StandDataBaseOperations
import ru.obolonnyy.friendhelper.utils.database.StandRepository
import ru.obolonnyy.friendhelper.utils.getStringTimeStampWithDate
import java.util.Date

class StandRepositoryImpl(private val db: StandDataBaseOperations<StandEntityInt>
) : StandRepository {

    override suspend fun saveVersion(stand: StandI, version: String) {
        val elem = StandEntityImpl(
            timeStamp = Date().getStringTimeStampWithDate(),
            stringName = stand.stringName,
            version = version
        )
        db.insert(elem)
    }

    override suspend fun saveStatus(stand: StandI, status: String) {
        val elem = StandEntityImpl(
            timeStamp = Date().getStringTimeStampWithDate(),
            stringName = stand.stringName,
            status = status
        )
        db.insert(elem)
    }

    override suspend fun getAllData(): List<StandEntityInt> {
        return db.getAll()
    }
/*
     fun StandEntityImpl.toLocal() : StandEntityInt {
        return StandEntity(this.timeStamp, this.stringName, this.version, this.status)
    }*/
}