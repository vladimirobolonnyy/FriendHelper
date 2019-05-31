package ru.obolonnyy.friendhelper.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.obolonnyy.friendhelper.database.datamodel.StandEntityImpl

@Dao
interface MainElementDao {

    @Insert
    fun insert(elem: StandEntityImpl)

    @Insert
    fun insertAll(elem: List<StandEntityImpl>)

    @Query("SELECT * FROM StandEntityImpl ORDER BY stringName, roomId DESC")
    fun getAll(): List<StandEntityImpl>
}


