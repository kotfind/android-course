package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun PictureDetailsScreen(
    picture: Picture,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    var zoom by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableStateOf(0f) }

    var transformableState = rememberTransformableState {
        zoomChange,
        offsetChange,
        rotationChange,
        ->
        zoom *= zoomChange
        rotation += rotationChange

        // FIXME: correct based on rotation
        offset += offsetChange * zoom
    }

    var showInfo by remember { mutableStateOf(false) }

    Image(
        bitmap = picture.getImage(context).asImageBitmap(),
        contentDescription = null,
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(
                scaleX = zoom,
                scaleY = zoom,
                rotationZ = rotation,
                translationX = offset.x,
                translationY = offset.y,
            )
            .transformable(transformableState)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        zoom = 1f
                        offset = Offset.Zero
                        rotation = 0f
                    },
                    onLongPress = { showInfo = true },
                )
            }
    )

    if (showInfo) {
        AlertDialog(
            modifier = Modifier.padding(10.dp),
            title = { Text("Image Data") },
            text = {
                Column {
                    Text(
                        text = "Name:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(picture.name)

                    Text(
                        text = "Album Name:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(picture.albumName)

                    Text(
                        text = "Date Taken:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(picture.dateTaken.toString())

                    Text(
                        text = "Date Added:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(picture.dateAdded.toString())

                    Text(
                        text = "Width:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(picture.width.toString())

                    Text(
                        text = "Height:",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(picture.height.toString())
                }
            },
            onDismissRequest = { showInfo = false },
            confirmButton = {},
        )
    }
}
