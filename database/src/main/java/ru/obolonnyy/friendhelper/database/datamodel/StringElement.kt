package ru.obolonnyy.friendhelper.database.datamodel

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.obolonnyy.friendhelper.utils.database.StringElementInt


@Entity
class StringElement : StringElementInt {
    @NonNull
    @PrimaryKey
    override var id: String? = null
    override var name: String? = null
}