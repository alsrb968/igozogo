package io.jacob.igozogo.core.domain.model

import io.jacob.igozogo.core.domain.util.formatMillisAdaptive

data class PlayerProgress(
    val position: Long,
    val buffered: Long,
    val duration: Long,
) {
    val positionRatio: Float
        get() = position.toFloat() / duration

    val bufferedRatio: Float
        get() = buffered.toFloat() / duration

    val formattedPosition: String
        get() = position.formatMillisAdaptive()

    val formattedDuration: String
        get() = duration.formatMillisAdaptive()
}
