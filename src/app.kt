package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

import android.util.Log

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun App() {
    val context = LocalContext.current

    val helper = remember {
        NotificationHelper(context)
    }

    val notificationPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    Button(
        onClick = {
            if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                notificationPermissionState.launchPermissionRequest()
            }

            helper.showNotification("Title", "Lorem Ipsum")
        },
    ) {
        Text("Show Notification")
    }
}

class NotificationHelper(private val context: Context) {
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

        // val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) 
        //         as NotificationManager
        // notificationManager.createNotificationChannel(channel)

        NotificationManagerCompat
            .from(context)
            .createNotificationChannel(channel)
    }

    fun showNotification(title: String, msg: String) {
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
