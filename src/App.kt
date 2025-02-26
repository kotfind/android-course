package com.kotfind.android_course

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign

@Composable
fun App() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ColorFullBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = "I change my color",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }

        RotatingButton()

        HiddingButton()
    }
}

@Composable
fun ColorFullBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val hue by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000 /* ms */),
            repeatMode = RepeatMode.Reverse,
        )
    )

    val color = Color.hsv(hue, 1f, 1f)

    Box(modifier = modifier.background(color)) {
        content()
    }
}

@Composable
fun HiddingButton() {
    var isFirstHidden by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(
            visible = !isFirstHidden,
            enter = slideInVertically(),
            exit = slideOutVertically(),
        ) {
            Button(onClick = { isFirstHidden = true }) {
                Text(text = "First Button")
            }
        }

        AnimatedVisibility(
            visible = isFirstHidden,
            enter = slideInVertically(),
            exit = slideOutVertically(),
        ) {
            Button(onClick = { isFirstHidden = false }) {
                Text(text = "Second Button")
            }
        }
    }
}

@Composable
fun RotatingButton() {
    var angle by remember { mutableStateOf(0) }

    while (angle >= 360) {
        angle -= 360
    }

    AnimatedContent(targetState = angle) {
        Button(
            onClick = { angle += 15 },
            modifier = Modifier
                .rotate(it.toFloat())
                .size(300.dp)
        ) {
            Text("${it}deg")
        }
    }
}
