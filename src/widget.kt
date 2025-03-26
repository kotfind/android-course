package org.kotfind.android_course

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.layout.*
import androidx.glance.text.*

import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.glance.action.clickable
import android.content.Intent

@Composable
fun Widget() {
    val context = LocalContext.current

    Row(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(color = Color.White),

        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("${State.tags}")

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
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
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
