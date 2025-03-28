package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

import android.util.Log

@Composable
fun PictureDetailsScreen(
    album: Album,
    picture: Picture,
    modifier: Modifier = Modifier,
    onPictureChanged: (Picture) -> Unit,
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

    val dragThreshold = with(LocalDensity.current) { 30.dp.toPx() }
    var dragAmountSum by remember { mutableStateOf(0f) }
    // var dragPanelColor by remember { mutableStateOf(Color.Black) }

    val colorValue = min(1f, max(0f, abs(dragAmountSum) / dragThreshold))
    val dragPanelColor = if (dragAmountSum > 0) {
            Color(red = colorValue, green = 0f, blue = 0f)
        } else {
            Color(red = 0f, green = 0f, blue = colorValue)
        }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            bitmap = picture.getImage(context).asImageBitmap(),
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
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

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(dragPanelColor)
                .pointerInput(picture) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            dragAmountSum = 0f
                        },
                        onDragEnd = {
                            if (abs(dragAmountSum) > dragThreshold) {
                                val pic = if (dragAmountSum > 0) {
                                        album.getNextPicture(picture)
                                    } else {
                                        album.getPrevPicture(picture)
                                    }

                                if (pic != null) {
                                    onPictureChanged(pic)
                                    zoom = 1f
                                    offset = Offset.Zero
                                    rotation = 0f
                                }
                            }

                            dragAmountSum = 0f
                        },
                        onHorizontalDrag = { _, dragAmount ->
                            dragAmountSum += dragAmount
                        },
                    )
                },
            content = {},
        )
    }

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
