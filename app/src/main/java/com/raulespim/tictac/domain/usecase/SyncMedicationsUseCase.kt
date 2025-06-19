package com.raulespim.tictac.domain.usecase

import com.raulespim.tictac.data.repository.MedicationFirestoreRepository
import com.raulespim.tictac.data.repository.MedicationRepository
import kotlinx.coroutines.flow.first

class SyncMedicationsUseCase(
    private val localRepository: MedicationRepository,
    private val firestoreRepository: MedicationFirestoreRepository
) {
    suspend operator fun invoke(userId: String) {
        // Obter medicamentos locais
        val localMedications = localRepository.getMedications(userId).first()

        // Sincronizar com Firestore
        val mergedMedications = firestoreRepository.syncMedications(userId, localMedications)

        // Atualizar Room com os dados sincronizados
        mergedMedications.forEach { medication ->
            localRepository.addMedication(medication)
        }
    }
}