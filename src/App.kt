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
        Person("Joe"),
        Person("Jim"),
        Person("Jack"),
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
                navController.toPersonList()
            }
        )
    }
}
