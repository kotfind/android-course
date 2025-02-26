package com.kotfind.android_course

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun App() {
    Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
        Text("Hello, world")

        AsyncImage(
            model = "https://cataas.com/cat",
            contentDescription = "Cat",
        )
    }
}

