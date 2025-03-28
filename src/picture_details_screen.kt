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

@Composable
fun PictureDetailsScreen(
    picture: Picture,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    var zoom by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var transformableState = rememberTransformableState {
        zoomChange,
        offsetChange,
        rotationChange,
        ->
        zoom *= zoomChange
        rotation += rotationChange
        offset += offsetChange * zoom
    }

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
            .transformable(transformableState),
    )
}
