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