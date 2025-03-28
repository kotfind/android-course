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

    if (albums == null) {
        Text("Loading pictures...")
    } else {
        Master(albums!!)
    }
}

@Composable
fun Master(albums: List<Album>) {
    var screen by remember {
        mutableStateOf<Screen>(Screen.AlbumList())
    }

    val screen_ = screen
    when (screen_) {
        is Screen.AlbumList -> {
            AlbumListScreen(
                albums = albums,
                modifier = Modifier.fillMaxSize(),
                onAlbumSelected = {
                    screen = Screen.AlbumDetails(it)
                }
            )
        }

        is Screen.AlbumDetails -> {
            val album = screen_.album

            AlbumDetailsScreen(
                album = album,
                modifier = Modifier.fillMaxSize(),
                onPictureSelected = {
                    screen = Screen.PictureDetails(it)
                },
            )
        }

        is Screen.PictureDetails -> {
            val picture = screen_.picture

            PictureDetailsScreen(
                picture = picture,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

}

sealed class Screen {
    class AlbumList : Screen()

    class AlbumDetails(val album: Album) : Screen()

    class PictureDetails(val picture: Picture) : Screen()
}
