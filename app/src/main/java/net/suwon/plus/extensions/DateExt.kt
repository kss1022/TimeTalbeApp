package net.suwon.plus.extensions

import java.text.SimpleDateFormat
import java.util.*

private val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.KOREA)
private val dayFormat = SimpleDateFormat("dd", Locale.KOREA)



fun Date.toReadableDateString(): String = dateFormat.format(this)
fun Date.toReadableTimeString(): String = timeFormat.format(this)
fun Date.toReadableDayString(): String = dayFormat.format(this)