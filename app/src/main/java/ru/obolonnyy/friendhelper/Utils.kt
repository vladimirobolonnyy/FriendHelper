package ru.obolonnyy.friendhelper

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat

fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}