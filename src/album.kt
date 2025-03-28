package org.kotfind.android_course

import android.content.Context
import android.graphics.Bitmap

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
