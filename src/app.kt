package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import android.Manifest
import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
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
private fun Master(albums: List<Album>) {
    var screen by remember {
        mutableStateOf<Screen>(Screen.AlbumList())
    }

    BackPressHandler(screen, { screen = it })

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
                    screen = Screen.PictureDetails(
                        album = album,
                        picture = it,
                    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BackPressHandler(
    screen: Screen,
    onScreenChanged: (Screen) -> Unit,
) {
    val activity = LocalContext.current as Activity

    BackHandler(enabled = true) {
        when (screen) {
            is Screen.AlbumList -> {
                activity.finish()
            }

            is Screen.AlbumDetails -> {
                onScreenChanged(Screen.AlbumList())
            }

            is Screen.PictureDetails -> {
                onScreenChanged(Screen.AlbumDetails(screen.album))
            }
        }
    }
}

private sealed class Screen {
    class AlbumList : Screen()

    class AlbumDetails(val album: Album) : Screen()

    class PictureDetails(
        val album: Album,
        val picture: Picture
    ) : Screen()
}
