package com.kotfind.android_course

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HsvSlider(modifier: Modifier = Modifier, title: String, onChange: (Color) -> Unit) {
    var hValue by remember { mutableStateOf(0f) }

    LaunchedEffect(hValue) { onChange(Color.hsv(hValue, 1f, 1f)) }

    Column(
        modifier = modifier.padding(5.dp).clip(RoundedCornerShape(5.dp)).background(Color.White),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = "$title:",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(5.dp),
        )

        Slider(
            value = hValue,
            onValueChange = {
                hValue = it
                onChange(Color.hsv(hValue, 1f, 1f))
            },
            valueRange = 0f..360f,
            modifier = Modifier.fillMaxWidth().padding(5.dp),
        )
    }
}
