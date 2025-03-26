package org.kotfind.android_course

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

object State {
    var text by mutableStateOf("Initial")
}
