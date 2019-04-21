package ru.obolonnyy.friendhelper.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/** Converting from String to Date **/

private val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

fun String.getDateWithServerTimeStamp(): Date? {
    val dateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")  // IMP !!!
    try {
        return dateFormat.parse(this)
    } catch (e: ParseException) {
        return null
    }
}

/** Converting from Date to String**/
fun Date.getStringTimeStampWithDate(): String {
    val dateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormat.format(this)
}