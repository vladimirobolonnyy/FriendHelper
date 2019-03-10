package ru.obolonnyy.friendhelper.database.datamodel

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
class MainElement : ru.obolonnyy.friendhelper.utils.database.MainElementInt {
    @NonNull
    @PrimaryKey
    override var id: String? = null
    override var name: String? = null
    override var context: String? = null
    override var restRequest1: String? = null
    override var restRequest2: String? = null
    override var restRequestFile: String? = null
}