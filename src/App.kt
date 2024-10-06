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
import com.kotfind.android_course.PersonDetails
import com.kotfind.android_course.PersonList
import com.kotfind.android_course.Person

@Composable
fun App() {
    val people = remember { mutableStateListOf<Person>(
        Person("Joe"),
        Person("Jim"),
        Person("Jack"),
    ) }

    PersonList(
        people = people,
        onPersonSelected = { }
    )
}
