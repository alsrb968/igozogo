package io.jacob.igozogo.core.model

enum class RepeatMode(val value: Int) {
    OFF(0), ONE(1), ALL(2);

    companion object {
        fun fromValue(value: Int) = entries.first { it.value == value }
    }
}
