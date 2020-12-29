package ru.skillbranch.devintensive.extensions

import java.util.*
import java.text.SimpleDateFormat
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.shortFormat(): String? {
    val pattern = if (this.isSameDay(Date())) "HH:mm" else "dd.MM.yy"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.isSameDay(date: Date): Boolean {
    val day1 = this.time/ DAY
    val day2 = date.time/ DAY
    return day1 == day2
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date{
    var time = this.time

    time += when(units){
        TimeUnits.SECOND -> value* SECOND
        TimeUnits.MINUTE -> value* MINUTE
        TimeUnits.HOUR -> value* HOUR
        TimeUnits.DAY -> value* DAY
    }

    this.time = time

    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {

    val diffSec = date.getTime() - this.time
    var humanReadableDiff = ""
    when(diffSec){
        in -999..999 -> humanReadableDiff = "только что"
        in 1000..44999 -> humanReadableDiff = "несколько секунд назад"
        in 45000..74999 -> humanReadableDiff = "минуту назад"
        in 75000..(45*MINUTE) -> humanReadableDiff = "${TimeUnits.MINUTE.plural((diffSec/MINUTE).toInt())} назад"
        in (45* MINUTE)..(75*MINUTE) -> humanReadableDiff = "час назад"
        in (75*MINUTE)..(22* HOUR) -> humanReadableDiff = "${TimeUnits.HOUR.plural((diffSec/ HOUR).toInt())} назад"
        in (22* HOUR)..(26* HOUR) -> humanReadableDiff = "день назад"
        in (26* HOUR)..(360* DAY ) -> humanReadableDiff = "${TimeUnits.DAY.plural((diffSec/ DAY).toInt())} назад"
        in -44999..-1000 -> humanReadableDiff = "через несколько секунд"
        in -74999..-45000 -> humanReadableDiff = "через минуту"
        in (-45*MINUTE)..-75000 -> humanReadableDiff = "через ${TimeUnits.MINUTE.plural((diffSec/MINUTE).toInt())}"
        in (-75*MINUTE)..(-45* MINUTE) -> humanReadableDiff = "через час"
        in (-22* HOUR)..(-75*MINUTE) -> humanReadableDiff = "через ${TimeUnits.HOUR.plural((diffSec/ HOUR).toInt())}"
        in (-26* HOUR)..(-22* HOUR) -> humanReadableDiff = "через день"
        in (-360* DAY )..(-26* HOUR) -> humanReadableDiff = "через ${TimeUnits.DAY.plural((diffSec/ DAY).toInt())}"
        in Long.MIN_VALUE..(-360* DAY) -> humanReadableDiff = "более чем через год"

        else -> humanReadableDiff = "более года назад"
    }

    return humanReadableDiff
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    fun plural(value: Int): String {
        var result = "${abs(value)} "
        result += when (this) {
            SECOND -> pluriaze(value, "секунду", "секунды", "секунд")
            MINUTE -> pluriaze(value, "минуту", "минуты", "минут")
            HOUR -> pluriaze(value, "час", "часа", "часов")
            DAY -> pluriaze(value, "день", "дня", "дней")
        }
        return result
    }
}
