package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    var showHelp by remember {
        mutableStateOf(false)
    }
    
    TopAppBar(
        title = { NameCard() },
        actions = {
            IconButton(
                onClick = { showHelp = true }
            ) {
                Icon(
                    painter = painterResource(R.drawable.help),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                )
            }
        },
    ) 

    if (showHelp) {
        AlertDialog(
            modifier = Modifier.padding(10.dp),
            title = { Text("Help") },
            text = { HelpText() },
            onDismissRequest = { showHelp = false },
            confirmButton = {
                Button(
                    onClick = { showHelp = false },
                ) {
                    Text("Ok")
                }
            },
        )
    }
}

@Composable
private fun HelpText() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Text("TODO")
    }
}
