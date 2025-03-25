package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun App() {
    var photoUri by remember { mutableStateOf<PhotoUri>(PhotoUri.None) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            val photoUri_ = photoUri
            photoUri =
                if (photoUri_ is PhotoUri.InProgress && isSuccess) {
                    PhotoUri.Ready(photoUri_.uri)
                } else {
                    PhotoUri.None
                }
        }
    )

    val context = LocalContext.current

    Column(
        modifier = 
            Modifier
                .fillMaxSize()
                .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                val uri = createImageFile(context)
                photoUri = PhotoUri.InProgress(uri)
                cameraLauncher.launch(uri)
            }
        ) {
            Text("Take Picture")
        }

        val photoUri_ = photoUri
        if (photoUri_ is PhotoUri.Ready) {
            AsyncImage(
                model = photoUri_.uri,
                contentDescription = null,
            )
        }
    }
}

fun createImageFile(context: Context): Uri {
    val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Date())

    val contentValues = ContentValues().apply{
        put(MediaStore.MediaColumns.DISPLAY_NAME, "photo_${timestamp}")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    return uri!!
}

sealed class PhotoUri {
    object None : PhotoUri()

    data class InProgress(val uri: Uri) : PhotoUri()

    data class Ready(val uri: Uri) : PhotoUri()
}
