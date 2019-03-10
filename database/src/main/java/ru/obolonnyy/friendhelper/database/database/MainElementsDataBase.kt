package ru.obolonnyy.friendhelper.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.obolonnyy.friendhelper.database.dao.LogsHistoryDao
import ru.obolonnyy.friendhelper.database.dao.MainElementDao
import ru.obolonnyy.friendhelper.database.datamodel.MainElement
import ru.obolonnyy.friendhelper.database.datamodel.StringElement

@Database(
    entities = [MainElement::class, StringElement::class],
    version = 1,
    exportSchema = false
)
abstract class MainElementsDataBase : RoomDatabase() {
    abstract fun mainElementsDataBase(): MainElementDao
    abstract fun logsHistory(): LogsHistoryDao
}