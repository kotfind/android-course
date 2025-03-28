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

    fun getNextPicture(picture: Picture): Picture? {
        return getShiftedPicture(picture, 1)
    }

    fun getPrevPicture(picture: Picture): Picture? {
        return getShiftedPicture(picture, -1)
    }

    private fun getShiftedPicture(picture: Picture, shift: Int): Picture? {
        val idx = pictures.indexOf(picture)
        if (idx == -1) {
            throw IllegalArgumentException("Picture not found in album")
        }

        val newIdx = idx + shift

        if (newIdx >= 0 && newIdx < pictures.size) {
            return pictures[newIdx]
        } else {
            return null
        }
    }
}
