package com.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.kotfind.android_course.PlayerModel
import com.kotfind.android_course.PlayerControls

@Composable
fun App() {
    val context = LocalContext.current
    val playerModel = remember { PlayerModel(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        ShowAllAnimals(onClick = {
            playerModel.play(it.soundResId)
        })

        PlayerControls(playerModel)
    }
}
