package ru.obolonnyy.friendhelper.utils.database

interface BaseDataBaseOperations<T> {

    fun insert(elem: T)

    fun insertAll(elem: List<T>)

    fun getById(id: Int): T

    fun getAll(): List<T>

    fun clear()
}