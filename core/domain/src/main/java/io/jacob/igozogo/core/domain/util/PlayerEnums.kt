package io.jacob.igozogo.core.domain.util

enum class PlaybackState(val value: Int) {
    IDLE(1), BUFFERING(2), READY(3), ENDED(4);

    companion object {
        fun fromValue(value: Int) = entries.first { it.value == value }
    }
}

enum class RepeatMode(val value: Int) {
    OFF(0), ONE(1), ALL(2);

    companion object {
        fun fromValue(value: Int) = entries.first { it.value == value }
    }
}