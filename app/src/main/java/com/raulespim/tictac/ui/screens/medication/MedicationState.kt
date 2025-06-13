package com.raulespim.tictac.ui.screens.medication

import com.raulespim.tictac.domain.model.Medication

data class MedicationState(
    val medications: List<Medication> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showAddDialog: Boolean = false
)