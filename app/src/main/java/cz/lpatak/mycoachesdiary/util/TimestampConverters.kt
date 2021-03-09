package cz.lpatak.mycoachesdiary.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*


@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
fun convertDateToLong(date: String): Long {
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    return format.parse(date).time
}

fun convertLongToDate(time: Long): String {
    val date = Date(time * 1000)
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
    return format.format(date)
}

fun stringDateToTimestamp(str: String): Timestamp {
    val date = Date(convertDateToLong(str))
    return Timestamp(date)
}

