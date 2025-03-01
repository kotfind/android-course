package com.kotfind.android_course

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun App(setPrimary: (Color) -> Unit, setBackground: (Color) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(5.dp),
    ) {
        HsvSlider(
            modifier = Modifier.fillMaxWidth(),
            title = "Primary",
            onChange = { setPrimary(it) },
        )

        HsvSlider(
            modifier = Modifier.fillMaxWidth(),
            title = "Background",
            onChange = { setBackground(it) },
        )

        FontDemo(modifier = Modifier.fillMaxWidth())
    }
}
