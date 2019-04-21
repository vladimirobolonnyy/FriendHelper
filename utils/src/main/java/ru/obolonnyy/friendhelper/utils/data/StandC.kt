package ru.obolonnyy.friendhelper.utils.data

interface StandI {
    val stringName: String
    val url: String
    val engName: String
    val context: String
}

data class StandC(
    override val stringName: String,
    override val url: String,
    override val engName: String,
    override val context: String
) : StandI

interface StandEntityInt {
    val timeStamp: String
    val stringName: String
    val version: String?
    val status: String?
}

data class StandEntity(
    override val timeStamp: String,
    override val stringName: String,
    override val version: String?,
    override val status: String?
) : StandEntityInt