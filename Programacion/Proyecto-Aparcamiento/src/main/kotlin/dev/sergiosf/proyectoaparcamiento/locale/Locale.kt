package dev.sergiosf.proyectoaparcamiento.locale

import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun LocalDateTime.toLocalDateTime(): String {
    return this.format(
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault())
    )
}

fun Double.toLocalDouble(): String {
    return NumberFormat.getNumberInstance(Locale.getDefault()).format(this)
}

fun Double.toLocalMoney(): String {
    return NumberFormat.getNumberInstance(Locale.getDefault()).format(this)
}