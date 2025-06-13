package com.raulespim.tictac.ui.screens.medication

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.raulespim.tictac.domain.usecase.AddMedicationUseCase
import com.raulespim.tictac.domain.usecase.GetMedicationsUseCase
import com.raulespim.tictac.domain.usecase.UpdateMedicationStatusUseCase
import com.raulespim.tictac.ui.screens.medication.viewactions.MedicationEffect
import com.raulespim.tictac.ui.screens.medication.viewactions.MedicationIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface MedicationComponent {
    val state: StateFlow<MedicationState>
    val effects: Flow<MedicationEffect>
    fun onIntent(intent: MedicationIntent)
}

class MedicationComponentImpl(
    componentContext: ComponentContext,
    private val addMedicationUseCase: AddMedicationUseCase,
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val updateMedicationStatusUseCase: UpdateMedicationStatusUseCase,
    private val userId: String
) : MedicationComponent, ComponentContext by componentContext {

    private val _state = MutableStateFlow(MedicationState())
    override val state: StateFlow<MedicationState> = _state.asStateFlow()

    private val _effects = Channel<MedicationEffect>(Channel.BUFFERED)
    override val effects: Flow<MedicationEffect> = _effects.receiveAsFlow()

    private val scope = coroutineScope()

    init {
        scope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            getMedicationsUseCase.invoke(userId).onEach { medications ->
                _state.update { it.copy(medications = medications, isLoading = false) }
            }
        }
    }

    override fun onIntent(intent: MedicationIntent) {
        when (intent) {
            MedicationIntent.OnAddMedicationClicked -> _state.update { it.copy(showAddDialog = true) }
            MedicationIntent.OnDismissAddDialog -> _state.update { it.copy(showAddDialog = false) }
            is MedicationIntent.OnSaveMedication -> onSaveMedication(intent.name, intent.time, intent.frequency)
            is MedicationIntent.OnMedicationStatusChanged -> {}
        }
    }

    private fun onSaveMedication(name: String, time: Long, frequency: String) {
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                withContext(Dispatchers.IO) { addMedicationUseCase.invoke(userId, name, time, frequency) }
                _state.update { it.copy(isLoading = false, showAddDialog = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun onMedicationStatusChanged(medicationId: String, isTaken: Boolean) {
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                withContext(Dispatchers.IO) { updateMedicationStatusUseCase.invoke(medicationId, isTaken) }
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

}