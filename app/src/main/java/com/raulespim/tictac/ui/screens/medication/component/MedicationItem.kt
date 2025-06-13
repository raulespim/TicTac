package com.raulespim.tictac.ui.screens.medication.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raulespim.tictac.R
import com.raulespim.tictac.domain.model.Medication
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MedicationItem(
    medication: Medication,
    onStatusChanged: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = medication.isTaken,
                onCheckedChange = { onStatusChanged(it) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = medication.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        .format(Date(medication.time)),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = stringResource(R.string.frequency, medication.frequency),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}