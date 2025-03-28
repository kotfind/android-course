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
    var state by remember {
        mutableStateOf<State>(State.AlbumList())
    }

    val state_ = state
    when (state_) {
        is State.AlbumList -> {
            AlbumListScreen(
                albums = albums,
                modifier = Modifier.fillMaxSize(),
                onAlbumSelected = {
                    state = State.Album(it)
                }
            )
        }

        is State.Album -> {
            val album = state_.album

            AlbumScreen(
                album = album,
                modifier = Modifier.fillMaxSize(),
                onPictureSelected = { /* TODO */ },
            )
        }
    }

}

sealed class State {
    class AlbumList : State()

    class Album(val album: org.kotfind.android_course.Album) : State()
}
