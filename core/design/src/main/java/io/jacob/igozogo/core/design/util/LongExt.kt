package io.jacob.igozogo.core.design.util

import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import java.util.Locale

/**
 * Long을 디지털 시계 형식의 시간 문자열로 변환합니다.
 * 1시간 이상일 때는 H:MM:SS, 1시간 미만일 때는 MM:SS 형식으로 표시합니다.
 *
 * @receiver Long 밀리초 단위의 시간
 * @return String 디지털 시계 형식 (예: `1:23:45`, `05:30`)
 *
 * 예시:
 * - 125000L.toMediaTimeFormat() → "02:05" (2분 5초)
 * - 3661000L.toMediaTimeFormat() → "1:01:01" (1시간 1분 1초)
 * - 30000L.toMediaTimeFormat() → "00:30" (30초)
 * - 7380000L.toMediaTimeFormat() → "2:03:00" (2시간 3분)
 */
fun Long.toMediaTimeFormat(): String {
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

/**
 * Long을 로케일에 맞는 사람이 읽기 쉬운 시간 형식으로 변환합니다.
 *
 * @receiver Long 밀리초 단위의 시간 (예: 3661000 = 1시간 1분 1초)
 * @return String 로케일에 따른 형식 (ko: `1시간 1분 1초`, en: `1h 1m 1s`)
 *
 * 예시:
 * - 125000L.toHumanReadableTime() → "2분 5초" (한국어) / "2m 5s" (영어)
 * - 3661000L.toHumanReadableTime() → "1시간 1분 1초" (한국어) / "1h 1m 1s" (영어)
 * - 30000L.toHumanReadableTime() → "30초" (한국어) / "30s" (영어)
 */
fun Long.toHumanReadableTime(): String {
    val totalSeconds = (this / 1000).toInt()
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

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