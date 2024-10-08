package com.kotfind.android_course

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import com.kotfind.android_course.Person
import com.kotfind.android_course.PersonListRoute

@Composable
fun App() {
    val people = remember { mutableStateListOf<Person>(
        Person("Jace", 42.toUByte(), true),
        Person("Jack", 33.toUByte(), true),
        Person("Jackson", 58.toUByte(), true),
        Person("Jacob", 59.toUByte(), true),
        Person("Jade", 31.toUByte(), false),
        Person("Jade", 34.toUByte(), true),
        Person("James", 19.toUByte(), false),
        Person("James", 41.toUByte(), true),
        Person("Jameson", 32.toUByte(), true),
        Person("Jasmine", 47.toUByte(), false),
        Person("Jasper", 48.toUByte(), true),
        Person("Jaxon", 36.toUByte(), true),
        Person("Jayce", 30.toUByte(), false),
        Person("Jayden", 41.toUByte(), true),
        Person("Jayden", 50.toUByte(), false),
        Person("Jeremiah", 34.toUByte(), true),
        Person("Jesse", 39.toUByte(), false),
        Person("Jett", 25.toUByte(), false),
        Person("John", 43.toUByte(), true),
        Person("Jonah", 50.toUByte(), true),
        Person("Jonathan", 23.toUByte(), true),
        Person("Jordan", 32.toUByte(), true),
        Person("Jordan", 41.toUByte(), false),
        Person("Jordyn", 24.toUByte(), false),
        Person("Jose", 46.toUByte(), true),
        Person("Joseph", 52.toUByte(), true),
        Person("Josephine", 48.toUByte(), false),
        Person("Joshua", 33.toUByte(), true),
        Person("Josiah", 55.toUByte(), true),
        Person("Josie", 46.toUByte(), false),
        Person("Journee", 48.toUByte(), false),
        Person("Juan", 44.toUByte(), false),
        Person("Jude", 57.toUByte(), false),
        Person("Julia", 35.toUByte(), false),
        Person("Julian", 20.toUByte(), false),
        Person("Julian", 39.toUByte(), true),
        Person("Juliana", 36.toUByte(), false),
        Person("Juliette", 51.toUByte(), false),
        Person("June", 33.toUByte(), false),
        Person("Juniper", 54.toUByte(), false),
    ) }

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = PersonListRoute) {
        personListDest(
            people = people,
            onPersonSelected = { person_id ->
                navController.toPersonDetails(person_id)
            }
        )
        personDetailsDest(
            people = people,
            onBack = { person_id, new_person ->
                people[person_id] = new_person
                navController.navigate(PersonListRoute) {
                    popUpTo(PersonListRoute)
                }
            }
        )
    }
}
