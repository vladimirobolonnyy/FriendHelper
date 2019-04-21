package ru.obolonnyy.friendhelper.main.main

import java.io.File

data class MainViewState (
    var items: List<StandState>? = null,
    var file: File? = null
)