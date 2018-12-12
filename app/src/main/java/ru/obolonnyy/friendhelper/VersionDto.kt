package ru.obolonnyy.friendhelper

data class VersionDto(
        val version: String?,
        val boolean: Boolean?,
        val releaseNotes: Array<String>?
)

fun VersionDto.toLocal(): VersionLocal = VersionLocal(this.version ?: "error")

data class VersionLocal(val version: String)