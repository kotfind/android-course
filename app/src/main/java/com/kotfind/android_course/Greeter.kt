package com.kotfind.android_course

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun Greeter() {
    Column(modifier = Modifier.padding(all = 30.dp).fillMaxWidth()) {
        var name by remember { mutableStateOf("") }

        OutlinedTextField(
                value = name,
                label = { Text(stringResource(R.string.name)) },
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        var is_greeting_menu_expanded by remember { mutableStateOf(false) }
        var greeting by remember { mutableStateOf("Hello") }
        val greetings = stringArrayResource(R.array.greetings)

        Box(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopCenter)) {
            Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { is_greeting_menu_expanded = !is_greeting_menu_expanded }
            ) { Text(stringResource(R.string.select_greeting)) }

            DropdownMenu(
                    expanded = is_greeting_menu_expanded,
                    onDismissRequest = { is_greeting_menu_expanded = false },
            ) {
                for (greeting_item in greetings) {
                    DropdownMenuItem(
                            text = { Text(greeting_item) },
                            onClick = {
                                greeting = greeting_item
                                is_greeting_menu_expanded = false
                            },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (name.trim().isEmpty()) {
            Text(stringResource(R.string.dont_know_name))
        } else {
            Text(greeting + ", " + name + "!")
        }
    }
}
