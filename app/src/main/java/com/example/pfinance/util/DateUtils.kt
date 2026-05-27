package com.example.pfinance.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object DateUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val monthFormatter = DateTimeFormatter.ofPattern("yyyy年MM月")
    private val fullFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")

    fun formatDate(date: LocalDateTime): String = date.format(dateFormatter)
    fun formatMonth(date: LocalDateTime): String = date.format(monthFormatter)
    fun formatFull(date: LocalDateTime): String = date.format(fullFormatter)

    fun getFriendlyDate(date: LocalDateTime): String {
        val today = LocalDate.now()
        val d = date.toLocalDate()
        return when {
            d == today -> "今天 ${date.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            d == today.minusDays(1) -> "昨天 ${date.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            d == today.minusDays(2) -> "前天 ${date.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            d.year == today.year -> date.format(DateTimeFormatter.ofPattern("MM月dd日 HH:mm"))
            else -> date.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"))
        }
    }

    fun daysBetween(start: LocalDateTime, end: LocalDateTime): Long =
        ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate())

    fun getMonthRange(year: Int, month: Int): Pair<LocalDateTime, LocalDateTime> {
        val start = LocalDateTime.of(year, month, 1, 0, 0)
        val end = start.withDayOfMonth(start.toLocalDate().lengthOfMonth()).withHour(23).withMinute(59)
        return start to end
    }

    fun getCurrentMonthRange(): Pair<LocalDateTime, LocalDateTime> {
        val now = LocalDateTime.now()
        return getMonthRange(now.year, now.monthValue)
    }
}
