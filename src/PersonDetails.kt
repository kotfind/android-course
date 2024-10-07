package com.kotfind.android_course

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kotfind.android_course.Person
import com.kotfind.android_course.PersonDetailsRoute
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

    Column(modifier = Modifier.padding(10.dp).fillMaxSize()) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Button(onClick = { onBack(person) }) {
                Text("Cancel")
            }

            Spacer(Modifier.width(5.dp))

            Button(
                onClick = {
                    var new_person = person
                    new_person.name = name
                    onBack(new_person)
                }
            ) {
                Text("Apply")
            }
        }
    }
}
