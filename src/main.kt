package com.kotfind.android_course

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var primary by remember { mutableStateOf(Color(0, 0, 0)) }
            var background by remember { mutableStateOf(Color(0, 0, 0)) }

            Theme(primary = primary, background = background) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Column(modifier = Modifier.safeDrawingPadding()) {
                        NameCard()
                        App(setPrimary = { primary = it }, setBackground = { background = it })
                    }
                }
            }
        }
    }
}
