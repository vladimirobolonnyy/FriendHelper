package ru.obolonnyy.friendhelper.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import java.io.File

fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Context.getAvailableDownloadsDir(): File {
    return if (isExternalStorageAvailable() && isWriteExternalPermissionGranted(this)) {
        getExternalDownloadsDir()
    } else {
        getInternalStoragePublicDir(Environment.DIRECTORY_DOWNLOADS, this)
    }
}

private fun getInternalDir(context: Context): File = context.filesDir!!

private fun getExternalDownloadsDir(): File = getExtPublicDir(Environment.DIRECTORY_DOWNLOADS)

private fun getInternalStoragePublicDir(envDirectory: String, context: Context): File {
    val internalPictureDir = File(getInternalDir(context), envDirectory)
    if (!internalPictureDir.exists()) internalPictureDir.mkdir()
    return internalPictureDir
}

fun isExternalStorageAvailable(): Boolean {
    return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
}

private fun getExtPublicDir(envDirectory: String): File {
    val externalDir = Environment.getExternalStoragePublicDirectory(envDirectory)
    if (!externalDir.exists()) externalDir.mkdir()
    return externalDir
}

private fun isWriteExternalPermissionGranted(context: Context): Boolean {
    val permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    val result = context.checkCallingOrSelfPermission(permission)
    return (result == PackageManager.PERMISSION_GRANTED)
}