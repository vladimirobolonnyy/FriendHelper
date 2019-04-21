package ru.obolonnyy.friendhelper.database.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.obolonnyy.friendhelper.utils.data.StandEntityInt

@Entity
class StandEntityImpl : StandEntityInt {
    @PrimaryKey
    override var timeStamp: String = ""
    override var stringName: String = ""
    override var version: String? = null
    override var status: String? = null
}