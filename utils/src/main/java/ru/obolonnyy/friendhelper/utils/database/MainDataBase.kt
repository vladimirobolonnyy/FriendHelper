package ru.obolonnyy.friendhelper.utils.database

interface MainDataBase<T> {

    fun standDataBase(): StandDataBaseOperations<T>
}