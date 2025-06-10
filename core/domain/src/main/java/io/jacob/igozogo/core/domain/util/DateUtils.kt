package io.jacob.igozogo.core.domain.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun String.toHumanReadableDate(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    val dateTime = LocalDateTime.parse(this, inputFormatter)

    // Locale과 FormatStyle을 사용하여 DateTimeFormatter 생성
    // FormatStyle.LONG 또는 FormatStyle.MEDIUM이 일반적으로 "YYYY년 M월 D일" / "D MMMM YYYY"에 가깝습니다.
    val outputFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.getDefault())

    return dateTime.format(outputFormatter)
}