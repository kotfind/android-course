package com.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip

data class Animal(
    val name: String,
    val imgResId: Int,
    val soundResId: Int
)

private val animals = listOf(
    Animal("Bird", R.drawable.bird, R.raw.bird),
    Animal("Cow", R.drawable.cow, R.raw.cow),
    Animal("Dog", R.drawable.dog, R.raw.dog),
    Animal("Donkey", R.drawable.donkey, R.raw.donkey),
    Animal("Horse", R.drawable.horse, R.raw.horse),
    Animal("Rooster", R.drawable.rooster, R.raw.rooster),
)

@Composable
fun ShowAllAnimals(
    onClick: (Animal) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        for (animal in animals) {
            ShowAnimal(
                animal,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(animal) }
            )
        }
    }
}

@Composable
private fun ShowAnimal(
    animal: Animal,
    modifier: Modifier = Modifier,
) {
    with (animal) {
        Row(
            modifier = modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(5.dp),
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            Image(
                painter = painterResource(imgResId),
                contentDescription = "Image of $name",
                modifier = Modifier.size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Text(name)
        }
    }
}
