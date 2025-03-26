package org.kotfind.android_course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    var tags by remember {
        mutableStateOf<String>(State.tags)
    }

    var filter by remember {
        mutableStateOf<Filter>(State.filter)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            label = { Text("Tags") },
            placeholder = { Text("orange,cute") },
            value = tags,
            onValueChange = { tags = it },
            singleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
        )

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = filter.prettyName,
                onValueChange = {},
                label = { Text("Filter") },
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                for (filterClazz in Filter::class.sealedSubclasses) {
                    val filterInstance = filterClazz.objectInstance!!
                    DropdownMenuItem(
                        text = { Text(filterInstance.prettyName) },
                        onClick = {
                            filter = filterInstance
                            expanded = false
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                State.tags = tags
                State.filter = filter
            }
        ) {
            Text("Submit")
        }
    }
}
