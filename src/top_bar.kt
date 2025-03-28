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
    var showControlls by remember {
        mutableStateOf(false)
    }
    
    TopAppBar(
        title = { NameCard() },
        actions = {
            IconButton(
                onClick = { showControlls = true }
            ) {
                Icon(
                    painter = painterResource(R.drawable.help),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp),
                )
            }
        },
    ) 

    if (showControlls) {
        AlertDialog(
            modifier = Modifier.padding(10.dp),
            title = { Text("Controlls") },
            text = { ControllsText(controlls) },
            onDismissRequest = { showControlls = false },
            confirmButton = {
                Button(
                    onClick = { showControlls = false },
                ) {
                    Text("Ok")
                }
            },
        )
    }
}

private val controlls = Controlls(listOf(
    ControllsSection("Album List", listOf(
        ControllsShortcut(
            "Back Key",
            "Go to previous screen."
        ),
    )),

    ControllsSection("Album Details", listOf(
        ControllsShortcut(
            "Back Key",
            "Go to previous screen."
        ),
    )),

    ControllsSection("Picture Details (Image)", listOf(
        ControllsShortcut(
            "Back Key",
            "Go to previous screen."
        ),
        ControllsShortcut(
            "Pinch with two fingers",
            "Zoom the picture in/ out."
        ),
        ControllsShortcut(
            "Rotate with two fingers",
            "Rotate the picture."
        ),
        ControllsShortcut(
            "Drag with one finger",
            "Move the picture."
        ),
        ControllsShortcut(
            "Double Click",
            "Reset all transformations."
        ),
        ControllsShortcut(
            "Long Press",
            "Show info about the picture."
        ),
    )),

    ControllsSection("Picture Details (Bottom Bar)", listOf(
        ControllsShortcut(
            "Swipe Left",
            "Go to previous picture."
        ),
        ControllsShortcut(
            "Swipe Right",
            "Go to next picture."
        ),
    ))
))

@Composable
private fun ControllsText(controlls: Controlls) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    ) {
        for (section in controlls.sections) {
            Text(
                text = "${section.title}:",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
            )

            Column(
                modifier = Modifier.padding(start = 10.dp),
            ) {
                for (shortcut in section.shortcuts) {
                    Text(
                        text = shortcut.shortcut,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Text(
                        text = shortcut.description,
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }
        }
    }
}

private data class Controlls(
    val sections: List<ControllsSection>,
)

private data class ControllsSection(
    val title: String,
    val shortcuts: List<ControllsShortcut>,
)

private data class ControllsShortcut(
    val shortcut: String,
    val description: String,
)
