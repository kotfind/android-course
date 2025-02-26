package com.kotfind.android_course

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun App() {
    Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
        Text("Hello, world")

        ShowCat()
    }
}

@Composable
fun ShowCat() {
    val catViewModel: CatViewModel = viewModel()
    val cat by catViewModel.cat.collectAsState()

    LaunchedEffect(Unit) {
        catViewModel.fetchCat()
    }

    if (cat != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
        ) {
            with (cat!!) {
                AsyncImage(
                    model = url,
                    contentDescription = "Cat"
                )

                Text("ID: $id")
                Text("Tags: $tags")
                Text("Mimetype: $mimetype")
            }
        }
    } else {
        Text("Loading...")
    }
}

class CatViewModel : ViewModel() {
    private val _cat = MutableStateFlow<Cat?>(null)
    val cat: StateFlow<Cat?> = _cat

    fun fetchCat() {
        viewModelScope.launch {
            try {
                val fetchedCat = RetrofitClient.apiService.getCat()
                _cat.value = fetchedCat 
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class Cat(
    var id: String,
    var tags: List<String>,
    var url: String,
    var mimetype: String,
)

interface CatService {
    @GET("/cat?json=true")
    suspend fun getCat(): Cat
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
