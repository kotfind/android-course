package com.kotfind.android_course

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color

@Composable
fun Theme(
    primary: Color = lightColorScheme().primary,
    background: Color = lightColorScheme().background,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme =
            lightColorScheme(
                primary = primary,
                background = background,
            ),
        content = content)
}
