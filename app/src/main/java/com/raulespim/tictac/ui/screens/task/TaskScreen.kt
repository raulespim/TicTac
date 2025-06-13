package com.raulespim.tictac.ui.screens.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raulespim.tictac.ui.screens.task.component.TaskItem
import com.raulespim.tictac.ui.screens.task.viewactions.TaskIntent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskScreen(component: TaskComponent) {
    val state by component.state.collectAsStateWithLifecycle()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var time by remember { mutableStateOf(System.currentTimeMillis()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { component.onIntent(TaskIntent.OnAddTaskClicked) }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            LazyColumn {
                items(state.tasks) { task ->
                    TaskItem(
                        task = task,
                        onStatusChanged = { isCompleted ->
                            component.onIntent(TaskIntent.OnTaskStatusChanged(task.id, isCompleted))
                        }
                    )
                }
            }
            state.error?.let {
                Text(
                    text = state.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

    if (state.showAddDialog) {
        AlertDialog(
            onDismissRequest = { component.onIntent(TaskIntent.OnDismissAddDialog) },
            title = { Text("Adicionar Tarefa") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descrição (opcional)") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Placeholder para seleção de data/hora
                    Text(
                        text = "Hora: ${
                            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(time))
                        }"
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    component.onIntent(TaskIntent.OnSaveTask(title, description.takeIf { it.isNotBlank() }, time))
                    title = ""
                    description = ""
                }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                Button(onClick = { component.onIntent(TaskIntent.OnDismissAddDialog) }) {
                    Text("Cancelar")
                }
            }
        )
    }
}