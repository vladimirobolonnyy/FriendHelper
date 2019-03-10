package ru.obolonnyy.friendhelper.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.obolonnyy.friendhelper.database.datamodel.MainElement

@Dao
interface MainElementDao {

    @Insert
    fun insert(elem: MainElement)

    @Insert
    fun insertAll(elem: List<MainElement>)

    @Query("SELECT * FROM MainElement WHERE id = :elemId")
    fun fetchOneMoviesbyMovieId(elemId: Int): MainElement

    @Query("SELECT * FROM MainElement")
    fun getAll(): List<MainElement>
}


