package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext

import android.util.Log

import androidx.compose.ui.graphics.Color // DELETE ME

@Composable
fun App() {
    val context = LocalContext.current
    val registry = (context as ComponentActivity).activityResultRegistry

    var albums by remember {
        mutableStateOf<List<Album>?>(null)
    }

    LaunchedEffect(Unit) {
        withPermission(
            context,
            registry,
            Manifest.permission.READ_MEDIA_IMAGES
        ) {
            albums = getAlbums(context, registry)
        }
    }

    val albums_ = albums
    if (albums_ != null) {
        AlbumListScreen(
            albums = albums_,
            modifier = Modifier.fillMaxSize(),
            onAlbumSelected = { /* TODO */ }
        )
    }
}
