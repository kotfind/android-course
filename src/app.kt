package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert

@Composable
fun CompleteApp() {
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = { AppTopBar() },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar() {
    var expanded by remember { mutableStateOf(false) }
    
    TopAppBar(
        title = { NameCard() },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Options")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Settings") },
                    onClick = { /* Handle click */ expanded = false }
                )

                HorizontalDivider()

                DropdownMenuItem(
                    text = { Text("About") },
                    onClick = { /* Handle click */ expanded = false }
                )
            }
        },
    ) 
}

@Composable
fun AppBody() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Text("Hello, world!")
    }
}
