package com.kotfind.android_course

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kotfind.android_course.Person
import com.kotfind.android_course.PersonDetailsRoute
import kotlin.text.toUByte
import kotlinx.coroutines.flow.collect
import kotlinx.serialization.Serializable

@Serializable
data class PersonDetailsRoute(var person_id: Int)

fun NavGraphBuilder.personDetailsDest(people: List<Person>, onBack: (Int, Person) -> Unit) {
    composable<PersonDetailsRoute> {
        val (person_id) = it.toRoute<PersonDetailsRoute>()
        PersonDetailsScreen(
            person = people[person_id],
            onBack = { new_person ->
                onBack(person_id, new_person)
            }
        )
    }
}

fun NavController.toPersonDetails(person_id: Int) {
    navigate(route = PersonDetailsRoute(person_id))
}  

@Composable
internal fun PersonDetailsScreen(person: Person, onBack: (Person) -> Unit) {
    var name by remember { mutableStateOf(person.name) }
    var age by remember { mutableStateOf(person.age.toString()) }
    var is_male by remember { mutableStateOf(person.is_male) }
    var is_male_expanded by remember { mutableStateOf(false) }

    var age_num = try {
            age.trim().toUByte()
        } catch (e: NumberFormatException) {
            null
        }
        
    Column(
        modifier = Modifier.padding(10.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        // Title
        Text(
            text = "Person Details Screen",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        // Name
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )

        // Age
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            supportingText = {
                if (age_num == null) {
                    Text(
                        text = "Age should be a number in range from 0 to 255",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )

        // Gender
        Box(Modifier .fillMaxWidth()) {
            OutlinedTextField(
                readOnly = true,
                value = if (is_male) "Male" else "Female",
                onValueChange = {},
                label = { Text("Gender") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    is_male_expanded = !is_male_expanded
                                }
                            }
                        }
                    },
                trailingIcon = {
                    if (is_male_expanded) {
                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = ""
                        )
                    } else {
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = ""
                        )
                    }
                },
            )

            DropdownMenu(
                expanded = is_male_expanded,
                onDismissRequest = {
                    is_male_expanded = false
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                DropdownMenuItem(
                    text = { Text("Male") },
                    onClick = {
                        is_male = true
                        is_male_expanded = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                DropdownMenuItem(
                    text = { Text("Female") },
                    onClick = {
                        is_male = false
                        is_male_expanded = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(onClick = { onBack(person) }) {
                Text("Cancel")
            }

            Spacer(Modifier.width(5.dp))

            Button(
                enabled = age_num != null,
                onClick = {
                    var new_person = person
                    new_person.name = name
                    new_person.age = age_num ?: 0.toUByte();
                    new_person.is_male = is_male
                    onBack(new_person)
                }
            ) {
                Text("Apply")
            }
        }
    }
}
