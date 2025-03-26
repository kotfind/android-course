package org.kotfind.android_course

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.layout.*
import androidx.glance.text.*

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.glance.action.clickable
import androidx.glance.action.actionStartActivity

@Composable
fun Widget() {
    Row(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),

        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Image Placeholder")

        Column(
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier.fillMaxHeight(),
        ) {
            MyIconButton(
                iconResId = R.drawable.reload,
                onClick = {
                    // TODO
                }
            )

            MyIconButton(
                iconResId = R.drawable.save,
                onClick = {
                    // TODO
                }
            )

            MyIconButton(
                iconResId = R.drawable.settings,
                onClick = { actionStartActivity<MainActivity>() }
            )
        }
    }
}

@Composable
fun MyIconButton(
    iconResId: Int,
    onClick: () -> Unit,
) {
    Image(
        provider = ImageProvider(iconResId),
        contentDescription = null,
        modifier = GlanceModifier
            .size(30.dp)
            .clickable { onClick() },
    )
}
