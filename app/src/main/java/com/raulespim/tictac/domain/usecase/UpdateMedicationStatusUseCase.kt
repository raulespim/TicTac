package com.raulespim.tictac.domain.usecase

import com.raulespim.tictac.core.worker.SyncScheduler
import com.raulespim.tictac.data.repository.MedicationRepository

class UpdateMedicationStatusUseCase(
    private val medicationRepository: MedicationRepository
) {
    suspend operator fun invoke(medicationId: String, isTaken: Boolean) {
        medicationRepository.updateMedicationStatus(medicationId, isTaken)
        SyncScheduler.triggerSync()
    }
}