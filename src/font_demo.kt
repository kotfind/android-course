package com.kotfind.android_course

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun FontDemo(modifier: Modifier = Modifier) {
    var fontSizeFloat by remember { mutableStateOf(16.sp.value) }
    val fontSize = fontSizeFloat.sp

    var customFont by remember { mutableStateOf(customFonts.first()) }

    val textStyle =
        TextStyle(
            fontFamily = customFont.family,
            fontSize = fontSize,
        )

    var scrollState = rememberScrollState()
    val corutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.padding(5.dp).background(Color.White).verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Text(
            text = "Preview:",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(5.dp),
        )

        Text(
            text = stringResource(R.string.font_demo_text),
            color = MaterialTheme.colorScheme.primary,
            style = textStyle,
            modifier = Modifier.padding(5.dp),
        )

        Text(
            text = "Size:",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(5.dp),
        )

        Slider(
            value = fontSizeFloat,
            onValueChange = { fontSizeFloat = it },
            valueRange = 8.sp.value..100.sp.value,
            modifier = Modifier.fillMaxWidth().padding(5.dp),
        )

        Text(
            text = "Fonts:",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(5.dp),
        )

        Column(
            modifier = modifier.padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            for (font in customFonts) {
                Button(
                    onClick = {
                        customFont = font

                        corutineScope.launch { scrollState.animateScrollTo(0) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = font.name,
                        color = Color.White,
                        style =
                            TextStyle(
                                fontFamily = font.family,
                            ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(5.dp).fillMaxWidth(),
                    )
                }
            }
        }
    }
}

class CustomFont {
    val name: String
    val resId: Int
    val family: FontFamily

    constructor(name: String, resId: Int) {
        this.name = name
        this.resId = resId
        this.family = FontFamily(Font(resId))
    }
}

val customFonts =
    listOf(
        CustomFont("Quilted Butterfly", R.font.quilted_butterfly),
        CustomFont("Merkur", R.font.merkur),
        CustomFont("Tires", R.font.tires),
        CustomFont("Bling Ring", R.font.bling_ring),
        CustomFont("Good Vandal 2", R.font.good_vandal2),
        CustomFont("Dominatrix", R.font.dominatrix),
        CustomFont("Logic Twenty Five", R.font.logic_twenty_five),
        CustomFont("Monster Party", R.font.monster_party),
        CustomFont("Summer Low Rank", R.font.summer_low_rank),
    )
