package ru.obolonnyy.friendhelper

import ru.obolonnyy.friendhelper.utilsandroid.BuildConfig

object BuildInfo {
    val isDebug: Boolean = BuildConfig.DEBUG
    val isAplha = BuildConfig.BUILD_TYPE == "alphaRelease"
    val isRelease = BuildConfig.BUILD_TYPE == "release"
}