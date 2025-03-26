package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.gestures.*

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun App() {
    var photoList = remember {
        mutableStateListOf<Photo>()
    }

    var selectedPhoto by remember {
        mutableStateOf<Photo?>(null)
    }

    if (selectedPhoto != null) {
        AsyncImage(
            model = selectedPhoto!!.uri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    selectedPhoto = null
                },
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            FileList(
                photos = photoList,
                onClick = { photo ->
                    selectedPhoto = photo
                },
                modifier = Modifier.fillMaxSize(),
            )

            PhotoButton(
                onNewPhoto = { photo ->
                    photoList.add(photo)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun FileList(
    photos: List<Photo>,
    onClick: (Photo) -> Unit,
    modifier: Modifier = Modifier,
) {
    var scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .scrollable(scrollState, Orientation.Vertical),
    ) {
        for (photo in photos.asReversed()) {
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .clickable {
                        onClick(photo)
                    },

                verticalAlignment = Alignment.CenterVertically,

                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                AsyncImage(
                    model = photo.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .padding(5.dp),
                )

                Text(
                    text = photo.name,
                    modifier = Modifier.weight(1f, true),
                )
            }
        }
    }
}

@Composable
fun PhotoButton(
    onNewPhoto: (Photo) -> Unit,
    modifier: Modifier = Modifier,
) {
    var photo by remember { mutableStateOf<Photo?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            if (!isSuccess) {
                return@rememberLauncherForActivityResult
            }

            if (photo != null) {
                onNewPhoto(photo!!)
            }

            photo = null
        }
    )

    val context = LocalContext.current

    IconButton(
        onClick = {
            val photo_ = createPhoto(context)
            photo = photo_
            cameraLauncher.launch(photo_.uri)
        },
        modifier = modifier
            .size(40.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clip(CircleShape)
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
    ) {
        Icon(
            painter = painterResource(R.drawable.camera),
            contentDescription = "Take a photo",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

fun createPhoto(context: Context): Photo {
    val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Date())
    val name = "photo_${timestamp}"

    val contentValues = ContentValues().apply{
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    val resolver = context.contentResolver

    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    return Photo(
        uri = uri!!,
        name = name
    )
}

data class Photo(
    val uri: Uri,
    val name: String,
)
