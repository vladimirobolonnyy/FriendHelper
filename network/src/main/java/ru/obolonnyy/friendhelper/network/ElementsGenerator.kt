package ru.obolonnyy.friendhelper.network

import ru.obolonnyy.friendhelper.api.Stand
import ru.obolonnyy.priv.network.StandEnum

object ElementsGenerator {
    fun initElements(): List<Stand> {
        val result = mutableListOf<Stand>()
        StandEnum.values().forEach { result.add(Stand(it.stringName, it.url, it.engName, it.context, it.app)) }
        return result
    }
}