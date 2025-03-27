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
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.media.AudioAttributes
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.text.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val context = LocalContext.current

    val channel = remember {
        MyNotificationChannel(
            context = context,
            registry = (context as ComponentActivity).activityResultRegistry,
            baseId = context.packageName + ".notification_channel_asldkfja",
            name = "My Notification Channel",
        )
    }

    var id by remember {
        mutableStateOf(0)
    }

    var title by remember {
        mutableStateOf("")
    }

    var text by remember {
        mutableStateOf("")
    }

    var withIcon by remember {
        mutableStateOf(true)
    }

    var sound by remember {
        mutableStateOf(Sound.all.first())
    }
    // TODO sound

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            label = { Text("Id") },
            placeholder = { Text("0") },
            value = id.toString(),
            onValueChange = {
                val newId = it.toIntOrNull()
                if (newId != null && newId >= 0) {
                    id = newId
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
        )

        OutlinedTextField(
            label = { Text("Title") },
            placeholder = { Text("Title") },
            value = title,
            onValueChange = { title = it },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
        )

        OutlinedTextField(
            label = { Text("Text") },
            placeholder = { Text("Text") },
            value = text,
            onValueChange = { text = it },
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
        )

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                value = sound.name,
                onValueChange = {},
                label = { Text("Sound") },
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                for (sound_ in Sound.all) {
                    DropdownMenuItem(
                        text = { Text(sound_.name) },
                        onClick = {
                            sound = sound_
                            expanded = false
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = withIcon,
                onCheckedChange = { withIcon = it }
            )

            Text("With Icon")
        }

        Button(
            onClick = {
                channel.MyNotification(
                    id = id,
                    title = title,
                    text = text,
                    iconResId = if (withIcon) R.drawable.profile_pic else null,
                    soundResId = sound.resId,
                ).show()
            }
        ) {
            Text("Notify")
        }
    }
}

class MyNotificationChannel(
    private val context: Context,
    private val registry: ActivityResultRegistry,
    private val baseId: String,
    private val name: String,
    private val importance: Int = NotificationManager.IMPORTANCE_HIGH,
) {
    companion object {
        var channel_id_suffix: Int = 0;
    }

    // New channel is created on each call, so that sound can be changed
    // Returns channel id
    fun createChannel(soundResId: Int? = null): String {
        val id = baseId + ":" + (channel_id_suffix++)

        val channel = NotificationChannel(
            id,
            name,
            importance
        )

        if (soundResId != null) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()

            val soundUri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(context.packageName)
                .path(soundResId.toString())
                .build()

            channel.setSound(soundUri, audioAttributes)
        }

        NotificationManagerCompat
            .from(context)
            .createNotificationChannel(channel)

        return id
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
        val soundResId: Int? = null,
    ) {
        fun show() {
            withNotificationPermission {
                val channelId = createChannel(soundResId)

                var builder = NotificationCompat.Builder(context, channelId)
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
                        id,
                        builder.build()
                    )
            }
        }
    }
}

data class Sound(
    val resId: Int,
    val name: String,
) {
    companion object {
        val all = listOf(
            Sound(R.raw.bird, "Bird"),
            Sound(R.raw.cow, "Cow"),
            Sound(R.raw.dog, "Dog"),
            Sound(R.raw.donkey, "Donkey"),
            Sound(R.raw.horse, "Horse"),
            Sound(R.raw.rooster, "Rooster"),
        )
    }
}
