package com.raulespim.tictac.ui.screens.medication

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
import com.raulespim.tictac.ui.screens.medication.component.MedicationItem
import com.raulespim.tictac.ui.screens.medication.viewactions.MedicationIntent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MedicationScreen(component: MedicationComponent) {
    val state by component.state.collectAsStateWithLifecycle()
    var name by remember { mutableStateOf("") }
    var time by remember { mutableStateOf(System.currentTimeMillis()) }
    var frequency by remember { mutableStateOf("DAILY") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { component.onIntent(MedicationIntent.OnAddMedicationClicked) }) {
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
                items(state.medications) { medication ->
                    MedicationItem(
                        medication = medication,
                        onStatusChanged = { isTaken ->
                            component.onIntent(MedicationIntent.OnMedicationStatusChanged(medication.id, isTaken))
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
            onDismissRequest = { component.onIntent(MedicationIntent.OnDismissAddDialog) },
            title = { Text("Adicionar Medicamento") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Placeholder para seleção de data/hora
                    Text(
                        text = "Hora: ${
                            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(time))
                        }"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = frequency,
                        onValueChange = { frequency = it },
                        label = { Text("Frequência (ex.: DAILY, WEEKLY)") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    component.onIntent(MedicationIntent.OnSaveMedication(name, time, frequency))
                    name = ""
                    frequency = "DAILY"
                }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                Button(onClick = { component.onIntent(MedicationIntent.OnDismissAddDialog) }) {
                    Text("Cancelar")
                }
            }
        )
    }
}