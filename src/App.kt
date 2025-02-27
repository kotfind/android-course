package com.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.HttpException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException

@Composable
fun App() {
    val catViewModel: CatViewModel = viewModel()

    val cat by catViewModel.cat.collectAsState()
    val error by catViewModel.error.collectAsState()
    val is_loading by catViewModel.is_loading.collectAsState()

    var tags by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        TextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text("Tags") },
            placeholder = { Text("orange,cute") },
            modifier = Modifier.fillMaxWidth(),
        )

        Button(
            onClick = { catViewModel.fetchCat(tags) },
            enabled = !is_loading,
        ) {
            Text("Submit")
        }

        when {
            cat != null -> {
                ShowCat(cat!!)
            }
            error != null -> {
                ErrorDialog(
                    error = error!!,
                    onDismiss = { catViewModel.unsetError() }
                )
            }
            is_loading -> {
                Text("Loading...")
            }
        }
    }
}

@Composable
fun ErrorDialog(error: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Error")
        },
        text = {
            Text("Error occured: $error")
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Ok")
            }
        }
    )
}

@Composable
fun ShowCat(cat: Cat) {
    with (cat) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
        ) {
            AsyncImage(
                model = url,
                contentDescription = "Cat"
            )

            Text("ID: $id")
            Text("Tags: ${tags.joinToString(",")}")
            Text("Mimetype: $mimetype")
        }
    }
}

class CatViewModel : ViewModel() {
    private val _cat = MutableStateFlow<Cat?>(null)
    private val _error = MutableStateFlow<String?>(null)
    private val _is_loading = MutableStateFlow<Boolean>(false)

    val cat: StateFlow<Cat?> = _cat
    val error: StateFlow<String?> = _error
    val is_loading: StateFlow<Boolean> = _is_loading

    fun fetchCat(tags: String) {
        _is_loading.value = true

        var tagsFinal = tags.trim()
        if (tagsFinal.isEmpty()) {
            tagsFinal = ","
        }

        viewModelScope.launch {
            try {
                val fetchedCat = RetrofitClient.apiService.getCat(tagsFinal)
                _cat.value = fetchedCat 
                _error.value = null
            } catch (e: IOException) {
                _cat.value = null
                _error.value = "Netwrok error: ${e.message}"
            } catch (e: HttpException) {
                _cat.value = null
                _error.value = "HTTP error: ${e.code()} - ${e.message}"
            } catch (e: Exception) {
                _cat.value = null
                _error.value = "Unknown error: ${e.message}"
            } finally {
                _is_loading.value = false
            }
        }
    }

    fun unsetError() {
        _error.value = null
    }
}

data class Cat(
    var id: String,
    var tags: List<String>,
    var url: String,
    var mimetype: String,
)

interface CatService {
    @GET("/cat/{tags}?json=true")
    suspend fun getCat(@Path("tags") tags: String): Cat
}

object RetrofitClient {
    private const val BASE_URL = "https://cataas.com"

    val apiService: CatService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatService::class.java)
    }
}
