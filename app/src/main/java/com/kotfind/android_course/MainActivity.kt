package com.kotfind.android_course

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.TextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.kotfind.android_course.ui.theme.MyTheme
import com.kotfind.android_course.NameCard

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column {
                        NameCard()
                        Body()
                    }
                }
            }
        }
    }
}

@Composable
fun Body() {
    Column(
        modifier = Modifier
            .padding(all = 30.dp)
            .fillMaxWidth()
    ) {
        var name by remember { mutableStateOf("") }

        OutlinedTextField(
            value = name,
            label = { Text("Name") },
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        var is_greeting_menu_expanded by remember { mutableStateOf(false) }
        var greeting by remember { mutableStateOf("Hello") }

        Box(
            modifier = Modifier.fillMaxWidth()
                .wrapContentSize(Alignment.TopCenter)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { is_greeting_menu_expanded = !is_greeting_menu_expanded }
            ) {
                Text("Select greeting")
            }

            DropdownMenu(
                expanded = is_greeting_menu_expanded,
                onDismissRequest = { is_greeting_menu_expanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text("Hello") },
                    onClick = {
                        greeting = "Hello"
                        is_greeting_menu_expanded = false
                    },
                )

                DropdownMenuItem(
                    text = { Text("今日は") },
                    onClick = {
                        greeting = "今日は"
                        is_greeting_menu_expanded = false
                    },
                )

                DropdownMenuItem(
                    text = { Text("Guten Tag") },
                    onClick = {
                        greeting = "Guten Tag"
                        is_greeting_menu_expanded = false
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (name.trim().isEmpty()) {
            Text("I don't know your name :(")
        } else {
            Text(greeting + ", " + name + "!")
        }
    }
}
