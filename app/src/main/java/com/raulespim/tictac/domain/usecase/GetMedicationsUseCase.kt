package com.raulespim.tictac.domain.usecase

import com.raulespim.tictac.data.repository.MedicationRepository
import com.raulespim.tictac.domain.model.Medication
import kotlinx.coroutines.flow.Flow

class GetMedicationsUseCase(
    private val medicationRepository: MedicationRepository
) {
    operator fun invoke(userId: String): Flow<List<Medication>> {
        return medicationRepository.getMedications(userId)
    }
}
