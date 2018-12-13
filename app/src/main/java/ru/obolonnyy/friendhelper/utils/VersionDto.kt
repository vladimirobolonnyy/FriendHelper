package ru.obolonnyy.friendhelper.utils

import ru.obolonnyy.friendhelper.utils.Constants.EMPTY_VERSION

data class VersionDto(
        val version: String?,
        val boolean: Boolean?,
        val releaseNotes: Array<String>?
)

fun VersionDto.toLocal(): VersionLocal = VersionLocal(this.version
    ?: EMPTY_VERSION)

data class VersionLocal(val version: String)