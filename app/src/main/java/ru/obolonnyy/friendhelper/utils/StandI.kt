package ru.obolonnyy.friendhelper.utils

interface StandI {
    val stringName: String
    val url: String
    val engName: String
}

data class StandC(override val stringName: String, override val url: String, override val engName: String) : StandI