package org.kotfind.android_course

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.layout.*
import androidx.glance.text.*

import androidx.compose.material3.MaterialTheme

@Composable
fun Widget() {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),

        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(State.tags)
        Text(State.filter.prettyName)
        Text(State.says)
    }
}
