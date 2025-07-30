package io.jacob.igozogo.core.domain.util

import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/**
 * String을 사람이 읽을 수 있는 날짜 형식으로 변환합니다.
 * @receiver String yyyyMMddHHmmss 형식의 날짜 문자열 (예: 20230101235959)
 * @return String ko: YYYY년 M월 D일, en: D MMMM YYYY
 */
internal fun String.toHumanReadableDate(): String {
    val inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    val dateTime = LocalDateTime.parse(this, inputFormatter)

    // Locale과 FormatStyle을 사용하여 DateTimeFormatter 생성
    // FormatStyle.LONG 또는 FormatStyle.MEDIUM이 일반적으로 "YYYY년 M월 D일" / "D MMMM YYYY"에 가깝습니다.
    val outputFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.getDefault())

    return dateTime.format(outputFormatter)
}

/**
 * Int를 사람이 읽을 수 있는 시간 형식으로 변환합니다.
 * @receiver Int 초 단위의 시간 (예: 3661)
 * @return String ko: 1시간 1분, en: 1h 1m
 */
internal fun Int.toHumanReadableTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60

    val measures = mutableListOf<Measure>()
    if (hours > 0)
        measures.add(Measure(hours, MeasureUnit.HOUR))
    if (minutes > 0)
        measures.add(Measure(minutes, MeasureUnit.MINUTE))
    if ((hours == 0 && minutes == 0) || seconds > 0)
        measures.add(Measure(seconds, MeasureUnit.SECOND))

    val formatter = MeasureFormat.getInstance(Locale.getDefault(), MeasureFormat.FormatWidth.SHORT)
    return formatter.formatMeasures(*measures.toTypedArray())
}

/**
 * Long을 시간 형식으로 변환합니다.
 * @receiver Long 밀리초 단위의 시간
 * @return String 시간 형식 시간
 */
internal fun Long.formatMillisAdaptive(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}