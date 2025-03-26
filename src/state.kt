package org.kotfind.android_course

import androidx.compose.runtime.*

object State {
    var tags by mutableStateOf<String>("")

    var filter by mutableStateOf<Filter>(Filter.None)
}

sealed class Filter {
    abstract val value: String?
    abstract val prettyName: String

    object Mono : Filter() {
        override val value = "mono"
        override val prettyName = "Mono"
    }

    object Negate : Filter() {
        override val value = "negate"
        override val prettyName = "Negate"
    }

    object None : Filter() {
        override val value = null
        override val prettyName = "None"
    }
}
