package org.kotfind.android_course

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Size
import android.graphics.ImageDecoder

data class Picture(
    val id: Long,
    val name: String,
    val uri: Uri,
) {
    private var thumbBitmap: Bitmap? = null

    private var bitmap: Bitmap? = null

    fun getThumb(context: Context): Bitmap {
        if (thumbBitmap == null) {
            thumbBitmap = context
                .contentResolver
                .loadThumbnail(
                    uri,
                    Size(thumbSize, thumbSize),
                    null
                )
        }

        return thumbBitmap!!
    }

    fun getImage(context: Context): Bitmap {
        if (bitmap == null) {
            val decoderSource = ImageDecoder.createSource(
                context.contentResolver,
                uri
            )

            bitmap = ImageDecoder.decodeBitmap(decoderSource)
        }

        return bitmap!!
    }

    companion object {
        const val thumbSize = 512

        private var emptyThumbBitmap: Bitmap? = null

        fun getEmptyThumb(context: Context): Bitmap {
            if (emptyThumbBitmap == null) {
                val origBitmap = BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.no_image,
                )

                val scaledBitmap = Bitmap.createScaledBitmap(
                    origBitmap,
                    thumbSize,
                    thumbSize,
                    true
                )

                emptyThumbBitmap = scaledBitmap
            }

            return emptyThumbBitmap!!
        }
    }
}

data class Album(
    val id: Long,
    val name: String,
    val pictures: List<Picture>,
) {
    fun getThumb(context: Context): Bitmap {
        if (pictures.isEmpty()) {
            return Picture.getEmptyThumb(context)
        } else {
            return pictures.first().getThumb(context)
        }
    }
}
