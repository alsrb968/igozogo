package io.jacob.igozogo.core.data.util

import kotlinx.datetime.*

/**
 * `yyyyMMddHHmmss` 형식의 문자열을 Instant로 변환합니다.
 * @receiver String yyyyMMddHHmmss 형식의 날짜 문자열 (예: 20230101235959)
 * @return Instant 변환된 시간
 */
internal fun String.toInstantAsSystemDefault(): Instant {
    require(this.length == 14) { "Date string must be 14 characters: $this" }

    val localDateTime = LocalDateTime(
        year = substring(0, 4).toInt(),
        monthNumber = substring(4, 6).toInt(),
        dayOfMonth = substring(6, 8).toInt(),
        hour = substring(8, 10).toInt(),
        minute = substring(10, 12).toInt(),
        second = substring(12, 14).toInt()
    )

    return localDateTime.toInstant(TimeZone.currentSystemDefault())
}

/**
 * Instant을 `yyyyMMddHHmmss` 형식의 문자열로 변환합니다.
 * @receiver Instant 변환할 시간
 * @return String yyyyMMddHHmmss 형식의 날짜 문자열 (예: 20230101235959)
 */
internal fun Instant.toDateTimeString(): String {
    val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())

    return buildString {
        append(localDateTime.year.toString().padStart(4, '0'))
        append(localDateTime.monthNumber.toString().padStart(2, '0'))
        append(localDateTime.dayOfMonth.toString().padStart(2, '0'))
        append(localDateTime.hour.toString().padStart(2, '0'))
        append(localDateTime.minute.toString().padStart(2, '0'))
        append(localDateTime.second.toString().padStart(2, '0'))
    }
}