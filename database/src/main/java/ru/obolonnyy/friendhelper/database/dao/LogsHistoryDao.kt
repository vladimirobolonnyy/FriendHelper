package ru.obolonnyy.friendhelper.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.obolonnyy.friendhelper.database.datamodel.StringElement

@Dao
interface LogsHistoryDao {

    @Insert
    fun insert(elem: StringElement)

    @Insert
    fun insertAll(elem: List<StringElement>)

    @Query("SELECT * FROM StringElement")
    fun getAll(): List<StringElement>

    @Query("DELETE FROM StringElement")
    fun deleteAll()
}


