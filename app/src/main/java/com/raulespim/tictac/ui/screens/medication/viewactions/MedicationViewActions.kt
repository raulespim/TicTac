package com.raulespim.tictac.ui.screens.medication.viewactions

sealed class MedicationIntent {
    data object OnAddMedicationClicked : MedicationIntent()
    data object OnDismissAddDialog : MedicationIntent()
    data class OnSaveMedication(val name: String, val time: Long, val frequency: String) : MedicationIntent()
    data class OnMedicationStatusChanged(val medicationId: String, val isTaken: Boolean) : MedicationIntent()
}

sealed class MedicationEffect {

}