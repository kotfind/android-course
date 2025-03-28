package org.kotfind.android_course

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultRegistry
import androidx.core.content.FileProvider
import java.io.File
import java.util.Date

fun getAlbums(context: Context, registry: ActivityResultRegistry): List<Album> {
    assertPermission(context, Manifest.permission.READ_MEDIA_IMAGES)

    val fields = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.DATE_TAKEN,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.WIDTH,
        MediaStore.Images.Media.HEIGHT,
    )

    val sortOrder = "${MediaStore.Images.Media._ID} ASC"

    val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val cursor = context.contentResolver.query(
        queryUri,
        fields,
        null,
        null,
        sortOrder,
    )!!

    val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
    val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
    val albumIdCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
    val albumNameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
    val imgPathCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    val dateTakenCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
    val dateAddedCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
    val widthCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH)
    val heightCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT)

    val mutAlbums = mutableMapOf<Long, MutAlbum>()

    while (cursor.moveToNext()) {
        val id = cursor.getLong(idCol)
        val name = cursor.getString(nameCol)

        val albumId = cursor.getLong(albumIdCol)
        val albumName = cursor.getString(albumNameCol)

        val imgPath = cursor.getString(imgPathCol)
        val imgUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            File(imgPath),
        )

        val dateTaken = Date(cursor.getLong(dateTakenCol))
        val dateAdded = Date(cursor.getLong(dateAddedCol))
        val width = cursor.getInt(widthCol)
        val height = cursor.getInt(heightCol)

        val album = mutAlbums.getOrPut(albumId) {
            MutAlbum(albumId, albumName, mutableListOf())
        }

        album.pictures.add(Picture(
            id = id,
            name = name,
            uri = imgUri,
            albumName = albumName,
            dateTaken = dateTaken,
            dateAdded = dateAdded,
            width = width,
            height = height,
        ))
    }

    val albums = mutableListOf<Album>()
    for (mutAlbum in mutAlbums.values) {
        with (mutAlbum) {
            albums.add(Album(id, name, pictures))
        }
    }

    return albums
}

private data class MutAlbum(
    var id: Long,
    var name: String,
    var pictures: MutableList<Picture>,
)
