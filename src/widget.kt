package org.kotfind.android_course

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.layout.*
import androidx.glance.text.*

import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.glance.action.clickable
import android.content.Intent

import kotlinx.serialization.Serializable
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.request.*
import io.ktor.client.call.*
import kotlinx.coroutines.*
import android.graphics.*
import io.ktor.http.*

import android.util.Log

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore

@Composable
fun Widget() {
    val context = LocalContext.current

    var cat by remember { mutableStateOf<CatGetter.Cat?>(null) }

    val scope = rememberCoroutineScope()

    val reload: () -> Unit = {
        scope.launch {
            cat = CatGetter.getCat(
                tags = State.tags,
                filter = State.filter
            )
        };
    }

    LaunchedEffect(State.tags, State.filter) {
        reload()
    }

    Row(
        modifier = GlanceModifier
            .fillMaxSize(),

        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier.fillMaxHeight(),
        ) {
            MyIconButton(
                iconResId = R.drawable.reload,
                onClick = {
                    reload()
                }
            )

            MyIconButton(
                iconResId = R.drawable.save,
                onClick = {
                    val cat_ = cat
                    if (cat_ != null) {
                        writeBitmapToGallery(
                            context = context,
                            bitmap = cat_.bitmap,
                            fileName = cat_.id
                        )
                    }
                }
            )

            MyIconButton(
                iconResId = R.drawable.settings,
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }
            )
        }

        val cat_ = cat
        if (cat_ != null) {
            Image(
                provider = ImageProvider(cat_.bitmap),
                contentDescription = null,
                modifier = GlanceModifier.defaultWeight(),
            )
        }
    }
}

@Composable
fun MyIconButton(
    iconResId: Int,
    onClick: () -> Unit,
) {
    Image(
        provider = ImageProvider(iconResId),
        contentDescription = null,
        modifier = GlanceModifier
            .size(30.dp)
            .clickable { onClick() }
            .background(color = Color.White),
    )
}

object CatGetter {
    private val ktorClientJson = HttpClient(CIO) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    private val ktorClientRaw = HttpClient(CIO) {
        expectSuccess = true
        install(ContentNegotiation) {}
    }

    @Serializable
    private data class CatResponse(
        val id: String,
        val url: String,
    )

    data class Cat(
        val id: String,
        val url: String,
        val bitmap: Bitmap,
    )

    suspend fun getCat(
        tags: String,
        filter: Filter,
    ): Cat? {
        val catResponse = getCatResponse(
            tags = tags,
            filter = filter,
        )
        if (catResponse == null) {
            return null
        }
        val bitmap = getCatBitmap(catResponse.url)
        if (bitmap == null) {
            return null
        }
        return Cat(
            id = catResponse.id,
            url = catResponse.url,
            bitmap = bitmap
        )
    }

    // TODO: use args
    private suspend fun getCatResponse(
        tags: String,
        filter: Filter,
    ): CatResponse? {
        try {
            val url = buildUrl(
                tags = tags,
                filter = filter,
            )

            val resp = ktorClientJson.get(url) {
                headers {
                    append("Accept", "application/json")
                }
            }

            val body: CatResponse = resp.body()

            return body
        } catch (e: Exception) {
            Log.e("CatGetter", "failed to get info", e)
            return null
        }
    }

    private fun buildUrl(
        tags: String,
        filter: Filter,
    ): String {
        // Base
        var url = "https://cataas.com/cat"

        // Tags
        if (!tags.isEmpty()) {
            if (url.last() != '/') {
                url += "/";
            }
            url += "$tags"
        }

        // Default Opts
        url += "?json=true&type=small"

        // Filter
        if (filter != Filter.None) {
            url += "&filter=${filter.value}"
        }

        Log.e("buildUrl", "built url: $url")

        return url
    }

    private suspend fun getCatBitmap(url: String): Bitmap? {
        try {
            val resp = ktorClientRaw.get(url) {
                headers {
                    append("Accept", "image/*")
                }
            }

            val byteArray: ByteArray = resp.body()
            val bitmap = withContext(Dispatchers.IO) {
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            }

            return bitmap
        } catch (e: Exception) {
            Log.e("CatGetter", "failed to get bitmap", e)
            return null
        }
    }
}

fun writeBitmapToGallery(
    context: Context,
    bitmap: Bitmap,
    fileName: String,
) {
    try {
        val contentValues = ContentValues().apply{
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val resolver = context.contentResolver

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        var outputStream = resolver.openOutputStream(uri!!)!!

        outputStream.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    } catch (e: Exception) {
        Log.e("writeBitmapToGallery", "failed to write to gallery", e)
    }
}
