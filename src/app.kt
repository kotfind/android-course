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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

import android.util.Log

@Composable
fun App() {
    val context = LocalContext.current

    val helper = remember {
        NotificationHelper(
            context = context,
            registry = (context as ComponentActivity).activityResultRegistry,
        )
    }

    Button(
        onClick = {
            helper.showNotification("Title", "Lorem Ipsum")
        },
    ) {
        Text("Show Notification")
    }
}

class NotificationHelper(
    private val context: Context,
    private val registry: ActivityResultRegistry,
) {
    companion object {
        const val CHANNEL_ID = "kotfind_notifications"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_NAME = "My Notification Channel"
        const val CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            CHANNEL_IMPORTANCE
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
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
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

        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    fun showNotification(title: String, msg: String) {
        withNotificationPermission {
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.profile_pic)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat
                .from(context)
                .notify(NOTIFICATION_ID, notification)
        }
    }
}
