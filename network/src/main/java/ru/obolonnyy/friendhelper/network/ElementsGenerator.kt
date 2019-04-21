package ru.obolonnyy.friendhelper.network

import ru.obolonnyy.friendhelper.utils.local.StandC
import ru.obolonnyy.friendhelper.utils.local.StandI
import ru.obolonnyy.priv.network.Stand

object ElementsGenerator {
    fun initElements(): List<StandI> {
        val result = mutableListOf<StandC>()
        Stand.values().forEach { result.add(StandC(it.stringName, it.url, it.engName, it.context)) }
        return result
    }
}