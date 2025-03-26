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

@Composable
fun Widget() {
    val context = LocalContext.current

    var catUrl by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            catUrl = CatGetter.getCat()!!.url
            Log.e("MYWIDGET", catUrl!!)
        }
    }

    Row(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(color = Color.White),

        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("${catUrl}")

        Column(
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = GlanceModifier.fillMaxHeight(),
        ) {
            MyIconButton(
                iconResId = R.drawable.reload,
                onClick = {
                    // TODO
                }
            )

            MyIconButton(
                iconResId = R.drawable.save,
                onClick = {
                    // TODO
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
            .clickable { onClick() },
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

    suspend fun getCat(): Cat? {
        val catResponse = getCatResponse()
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
    // tags: String,
    // filter: Filter,
    // says: String,
    private suspend fun getCatResponse(): CatResponse? {
        try {
            val url = "https://cataas.com/cat?json=true"

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

    private suspend fun getCatBitmap(url: String): Bitmap? {
        try {
            val resp = ktorClientRaw.get(url) {
                headers {
                    append("Accept", "image/*")
                }
            }

            Log.e("MYLOG", resp.contentType().toString())

            val byteArray: ByteArray = resp.body()
            val bitmap = withContext(Dispatchers.IO) {
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            }
            Log.e("MYLOG", byteArray.size.toString())
            return bitmap
        } catch (e: Exception) {
            Log.e("CatGetter", "failed to get bitmap", e)
            return null
        }
    }
}
