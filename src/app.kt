package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import android.graphics.drawable.Icon

import android.util.Log

@Composable
fun App() {
    val context = LocalContext.current


    val channel = remember {
        MyNotificationChannel(
            context = context,
            registry = (context as ComponentActivity).activityResultRegistry,
            id = context.packageName + ".notification_channel",
            name = "My Notification Channel",
        )
    }

    Button(
        onClick = {
            channel.MyNotification(
                id = 0,
                title = "0",
                text = "With Icon",
            ).show()

            channel.MyNotification(
                id = 1,
                title = "1",
                text = "No Icon",
                iconResId = null,
            ).show()
        },
    ) {
        Text("Show Notification")
    }
}

class MyNotificationChannel(
    private val context: Context,
    private val registry: ActivityResultRegistry,
    private val id: String,
    private val name: String,
    private val importance: Int = NotificationManager.IMPORTANCE_HIGH,
) {
    init {
        val channel = NotificationChannel(
            id,
            name,
            importance
        )

        NotificationManagerCompat
            .from(context)
            .createNotificationChannel(channel)
    }

    private fun withNotificationPermission(
        onDenied: () -> Unit = {
            Log.e("TASK11", "notification permission denied")
        },
        onGranted: () -> Unit,
    ) {
        val PERMISSION = Manifest.permission.POST_NOTIFICATIONS

        if (ContextCompat.checkSelfPermission(
                context,
                PERMISSION
            ) == PackageManager.PERMISSION_GRANTED)
        {
            onGranted()
        }

        val permissionLauncher: ActivityResultLauncher<String> =
            registry.register(
                key = "permission_key",
                contract = ActivityResultContracts.RequestPermission(),
                callback = { isGranted ->
                    if (isGranted) {
                        onGranted()
                    } else {
                        onDenied()
                    }
                },
            )

        permissionLauncher.launch(PERMISSION)
    }

    inner class MyNotification(
        val id: Int = 0,
        val title: String = "My Notification",
        val text: String = "",
        val iconResId: Int? = R.drawable.profile_pic,
    ) {
        fun show() {
            withNotificationPermission {
                var builder = NotificationCompat.Builder(context, this@MyNotificationChannel.id)
                    .setSmallIcon(R.drawable.profile_pic)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    
                if (iconResId != null) {
                    builder = builder
                        .setLargeIcon(Icon.createWithResource(context, iconResId))
                        .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                }

                NotificationManagerCompat
                    .from(context)
                    .notify(
                        this@MyNotification.id,
                        builder.build()
                    )
            }
        }
    }
}
