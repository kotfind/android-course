package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset

@Composable
fun CompleteApp() {
    var showBody by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            AppTopBar(
                onShowBodyChanged = { showBody = it },
                showBody = showBody,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (showBody) {
                AppBody()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onShowBodyChanged: (Boolean) -> Unit,
    showBody: Boolean,
) {
    var expanded by remember { mutableStateOf(false) }

    var showAbout by remember {
        mutableStateOf(false)
    }
    
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
                    text = {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = showBody,
                                onCheckedChange = {},
                            )

                            Text("Show Body")
                        }
                    },
                    onClick = {
                        onShowBodyChanged(!showBody)
                        expanded = false
                    }
                )

                HorizontalDivider()

                DropdownMenuItem(
                    text = { Text("About") },
                    onClick = {
                        showAbout = true
                        expanded = false
                    }
                )
            }
        },
    ) 

    if (showAbout) {
        AlertDialog(
            modifier = Modifier.padding(10.dp),
            title = { Text("About") },
            text = { Text(
                "This is 'Task9' application " +
                "written by Savva Chubiy, BPI233."
            ) },
            onDismissRequest = { showAbout = false },
            confirmButton = {
                Button(
                    onClick = { showAbout = false },
                ) {
                    Text("Ok")
                }
            },
        )
    }
}

@Composable
fun AppBody() {
    var background by remember {
        mutableStateOf(NamedColor.initialBackground)
    }

    var foreground by remember {
        mutableStateOf(NamedColor.initialForeground)
    }

    var menusState by remember {
        mutableStateOf<MenusState>(MenusState.None)
    }

    var outerOffset by remember {
        mutableStateOf(DpOffset(0.dp, 0.dp))
    }

    var innerOffset by remember {
        mutableStateOf(DpOffset(0.dp, 0.dp))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(background.color)
                .align(Alignment.TopStart)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            menusState = MenusState.OuterOnly
                            outerOffset = DpOffset(it.x.dp, it.y.dp)
                        }
                    )
                },
        ) {
            Text(
                text = "Long press me!",
                color = foreground.color,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(20.dp)
            )
        }

        DropdownMenu(
            expanded = menusState.isOuterExpanded,
            onDismissRequest = { menusState = MenusState.None },
            offset = outerOffset
        ) {
            DropdownMenuItem(
                onClick = { /* Handled in text */ },
                text = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    innerOffset = DpOffset(it.x.dp, it.y.dp) + outerOffset
                                    menusState = MenusState.Background
                                }
                            }
                    ) {
                        Text("Background")
                    }
                },
            )

            DropdownMenuItem(
                onClick = { /* Handled in text */ },
                text = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTapGestures {
                                    innerOffset = DpOffset(it.x.dp, it.y.dp) + outerOffset
                                    menusState = MenusState.Foreground
                                }
                            }
                    ) {
                        Text("Foreground")
                    }
                },
            )
        }

        ColorMenu(
            expanded = menusState is MenusState.Background,
            onColorSelected = {
                background = it
                menusState = MenusState.None
            },
            onDismiss = { menusState = MenusState.None },
            offset = innerOffset
        )

        ColorMenu(
            expanded = menusState is MenusState.Foreground,
            onColorSelected = {
                foreground = it
                menusState = MenusState.None
            },
            onDismiss = { menusState = MenusState.None },
            offset = innerOffset
        )
    }
}

@Composable
fun ColorMenu(
    expanded: Boolean,
    onColorSelected: (NamedColor) -> Unit,
    onDismiss: () -> Unit,
    offset: DpOffset
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismiss() },
        offset = offset
    ) {
        for (color_ in NamedColor.all) {
            DropdownMenuItem(
                text = { Text(color_.name) },
                onClick = { onColorSelected(color_) },
            )
        }
    }
}

data class NamedColor(
    val name: String,
    val color: Color,
) {
    companion object {
        val initialBackground = NamedColor("LightGray", Color.LightGray)
        val initialForeground = NamedColor("Black", Color.Black)

        val all = listOf(
            initialBackground,
            initialForeground,
            NamedColor("Blue", Color.Blue),
            NamedColor("Cyan", Color.Cyan),
            NamedColor("DarkGray", Color.DarkGray),
            NamedColor("Gray", Color.Gray),
            NamedColor("Green", Color.Green),
            NamedColor("Magenta", Color.Magenta),
            NamedColor("Red", Color.Red),
            NamedColor("White", Color.White),
        )
    }
}

sealed class MenusState {
    object None : MenusState() {
        override val isOuterExpanded = false
        override val isForegroundExpanded = false
        override val isBackgroundExpanded = false
    }

    object OuterOnly : MenusState() {
        override val isOuterExpanded = true
        override val isForegroundExpanded = false
        override val isBackgroundExpanded = false
    }

    object Foreground : MenusState() {
        override val isOuterExpanded = true
        override val isForegroundExpanded = true
        override val isBackgroundExpanded = false
    }

    object Background : MenusState() {
        override val isOuterExpanded = true
        override val isForegroundExpanded = false
        override val isBackgroundExpanded = true
    }

    abstract val isOuterExpanded: Boolean
    abstract val isForegroundExpanded: Boolean
    abstract val isBackgroundExpanded: Boolean
}
