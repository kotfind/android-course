package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    var textLocal by remember {
        mutableStateOf(State.text)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = textLocal,
            onValueChange = { textLocal = it },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
        )

        Button(
            onClick = {
                State.text = textLocal
            }
        ) {
            Text("Set Text")
        }
    }
}
