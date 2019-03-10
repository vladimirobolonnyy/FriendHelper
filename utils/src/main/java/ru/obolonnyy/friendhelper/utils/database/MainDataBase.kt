package ru.obolonnyy.friendhelper.utils.database

interface MainDataBase<T> {

    fun mainElementsDataBase(): BaseDataBaseOperations<T>
    fun logsHistory(): BaseDataBaseOperations<T>
}