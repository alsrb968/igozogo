package io.jacob.igozogo.core.model

enum class PlaybackState(val value: Int) {
    IDLE(1), BUFFERING(2), READY(3), ENDED(4);

    companion object {
        fun fromValue(value: Int) = entries.first { it.value == value }
    }
}
