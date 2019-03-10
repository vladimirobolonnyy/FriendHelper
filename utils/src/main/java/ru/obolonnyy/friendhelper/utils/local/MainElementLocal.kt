package ru.obolonnyy.friendhelper.utils.local

import ru.obolonnyy.friendhelper.utils.database.MainElementInt

data class MainElementLocal(

    override var id: String? = null,
    override var name: String? = null,
    override var context: String? = null,
    override var restRequest1: String? = null,
    override var restRequest2: String? = null,
    override var restRequestFile: String? = null

) : MainElementInt