package ru.obolonnyy.friendhelper.utils.database

import ru.obolonnyy.friendhelper.utils.data.StandEntityInt
import ru.obolonnyy.friendhelper.utils.data.StandI

interface StandRepository {

    suspend fun saveVersion(stand: StandI, version: String)
    suspend fun saveStatus(stand: StandI, status: String)
    suspend fun getAllData(): List<StandEntityInt>
}