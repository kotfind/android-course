package org.kotfind.android_course

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

import android.util.Log

fun hasPermission(
    context: Context,
    permission: String,
): Boolean {
    return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
}

fun assertPermission(context: Context, permission: String) {
    if (!hasPermission(context, permission)) {
        throw IllegalStateException("permission '$permission' should have been granted")
    }
}

fun withPermission(
    context: Context,
    registry: ActivityResultRegistry,
    permission: String,
    onDenied: () -> Unit = {
        Log.e(
            "withNotificationPermission",
            "permission ${permission} onDenied"
        )
    },
    onGranted: () -> Unit,
) {
    if (hasPermission(context, permission)) {
        onGranted()
    }

    val permissionLauncher: ActivityResultLauncher<String> =
        registry.register(
            key = "smth",
            contract = ActivityResultContracts.RequestPermission(),
            callback = { isGranted ->
                if (isGranted) {
                    onGranted()
                } else {
                    onDenied()
                }
            },
        )

    permissionLauncher.launch(permission)
}
