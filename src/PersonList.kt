package com.kotfind.android_course

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.background
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.navigation.NavController
import com.kotfind.android_course.Person
import kotlinx.serialization.Serializable

@Serializable
object PersonListRoute

fun NavGraphBuilder.personListDest(people: List<Person>, onPersonSelected: (Int) -> Unit) {
    composable<PersonListRoute> {
        PersonListScreen(people, onPersonSelected)
    }
}

fun NavController.toPersonList() {
    navigate(route = PersonListRoute)
}  

@Composable
internal fun PersonListScreen(people: List<Person>, onPersonSelected: (Int) -> Unit) {
    Column(
        modifier = Modifier.padding(10.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Title
        Text(
            text = "Person List Screen",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(people) { i, person ->
                Row(
                    modifier = Modifier
                        .clickable {
                            onPersonSelected(i)
                        }
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = person.name,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.weight(6f),
                    )

                    Text(
                        text = person.age.toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(2f),
                    )

                    Text(
                        text = if (person.is_male) "M" else "F",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(2f),
                    )
                }

                if (i + 1 != people.size) {
                    HorizontalDivider()
                }
            }
        }
    }
}
