package io.jacob.igozogo.core.design.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/**
 * Instant를 사람이 읽을 수 있는 날짜 형식으로 변환합니다.
 * @receiver Instant
 * @return String ko: `YYYY년 M월 D일`, en: `D MMMM YYYY`
 */
fun Instant.toHumanReadableDate(): String {
    val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())

    // kotlinx-datetime → java.time 변환
    val javaLocalDateTime = java.time.LocalDateTime.of(
        localDateTime.year,
        localDateTime.monthNumber,
        localDateTime.dayOfMonth,
        localDateTime.hour,
        localDateTime.minute,
        localDateTime.second
    )

    val outputFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
        .withLocale(Locale.getDefault())

    return javaLocalDateTime.format(outputFormatter)
}