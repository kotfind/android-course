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
fun AlbumListScreen(
    albums: List<Album>,
    onAlbumSelected: (Album) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Albums",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
        )

        for (album in albums) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAlbumSelected(album) }
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    bitmap = album.getThumb(context).asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                )

                Text(
                    text = album.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 10.dp),
                )
            }
        }
    }
}
