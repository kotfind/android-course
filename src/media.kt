package org.kotfind.android_course

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultRegistry
import java.io.File

// fun getAlbums(
//     context: Context,
//     registry: ActivityResultRegistry,
// ): List<Album> {
//     assertPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
//
//     val fields = arrayOf(
//         MediaStore.Images.Media.BUCKET_ID,
//         MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
//         MediaStore.Images.Media.DATA,
//     )
//
//     val sortOrder = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} ASC"
//
//     val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//
//     val cursor = context.contentResolver.query(
//         queryUri,
//         fields,
//         null,
//         null,
//         sortOrder,
//     )!!
//
//     val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
//     val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
//     val coverPathCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//
//     val ans = mutableListOf<Album>()
//
//     while (cursor.moveToNext()) {
//         val id = cursor.getString(idCol)
//         val name = cursor.getString(nameCol)
//
//         val coverPath = cursor.getString(coverPathCol)
//         val coverUri = Uri.fromFile(File(coverPath))
//
//         ans.add(Album(id, name, coverUri))
//     }
//
//     return ans
// }

fun getAlbums(context: Context, registry: ActivityResultRegistry): List<Album> {
    assertPermission(context, Manifest.permission.READ_MEDIA_IMAGES)

    val fields = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATA,
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

    val mutAlbums = mutableMapOf<Long, MutAlbum>()

    while (cursor.moveToNext()) {
        val id = cursor.getLong(idCol)
        val name = cursor.getString(nameCol)

        val albumId = cursor.getLong(albumIdCol)
        val albumName = cursor.getString(albumNameCol)

        val imgPath = cursor.getString(imgPathCol)
        val imgUri = Uri.fromFile(File(imgPath))

        val album = mutAlbums.getOrPut(albumId) {
            MutAlbum(albumId, albumName, mutableListOf())
        }

        album.pictures.add(Picture(id, name, imgUri))
    }

    val albums = mutableListOf<Album>()
    for (mutAlbum in mutAlbums.values) {
        with (mutAlbum) {
            albums.add(Album(id, name, pictures))
        }
    }

    return albums
}

data class Album(
    val id: Long,
    val name: String,
    val pictures: List<Picture>,
)

private data class MutAlbum(
    var id: Long,
    var name: String,
    var pictures: MutableList<Picture>,
)

data class Picture(
    val id: Long,
    val name: String,
    val uri: Uri,
)
