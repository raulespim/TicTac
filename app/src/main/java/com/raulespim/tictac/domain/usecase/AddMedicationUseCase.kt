package com.raulespim.tictac.domain.usecase

import com.raulespim.tictac.core.worker.SyncScheduler
import com.raulespim.tictac.data.repository.MedicationRepository
import com.raulespim.tictac.domain.model.Medication
import java.util.UUID

class AddMedicationUseCase(
    private val medicationRepository: MedicationRepository
) {
    suspend operator fun invoke(
        userId: String,
        name: String,
        time: Long,
        frequency: String
    ) {
        val now = System.currentTimeMillis()
        val medication = Medication(
            id = generateMedicationId(),
            userId = userId,
            name = name,
            time = time,
            frequency = frequency,
            isTaken = false,
            createdAt = now,
            lastModified = now
        )
        medicationRepository.addMedication(medication)
        SyncScheduler.triggerSync()
    }

    private fun generateMedicationId(): String {
        return "MED-${UUID.randomUUID().toString().take(8).uppercase()}"
    }
}