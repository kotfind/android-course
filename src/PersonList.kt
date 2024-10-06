package com.kotfind.android_course

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotfind.android_course.Person

@Composable
fun PersonList(people: List<Person>, onPersonSelected: (Int) -> Unit) {
    LazyColumn(modifier = Modifier.padding(10.dp).fillMaxSize()) {
        itemsIndexed(people) { i, person ->
            Row(
                modifier = Modifier
                    .clickable {
                        onPersonSelected(i)
                    }
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(person.name)
            }

            if (i + 1 != people.size) {
                HorizontalDivider()
            }
        }
    }
}
