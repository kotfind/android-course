package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun PictureDetailsScreen(
    picture: Picture,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Image(
        modifier = modifier.fillMaxSize(),
        bitmap = picture.getImage(context).asImageBitmap(),
        contentDescription = null,
    )
}
