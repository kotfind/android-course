package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AlbumScreen(
    album: Album,
    onPictureSelected: (Picture) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Pictures from\n'${album.name}'",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )

        if (album.pictures.isEmpty()) {
            Text(
                text = "No Pictures",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 10.dp),
            )
        }

        for (picture in album.pictures) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPictureSelected(picture) }
                    .padding(5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    bitmap = picture.getThumb(context).asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                )

                Text(
                    text = picture.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 10.dp),
                )
            }
        }
    }
}
