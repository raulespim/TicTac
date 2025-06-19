package com.raulespim.tictac.data.repository

import com.raulespim.tictac.data.local.dao.MedicationDao
import com.raulespim.tictac.data.local.entity.MedicationEntity
import com.raulespim.tictac.domain.model.Medication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface MedicationRepository {
    suspend fun addMedication(medication: Medication)
    fun getMedications(userId: String): Flow<List<Medication>>
    suspend fun updateMedicationStatus(medicationId: String, isTaken: Boolean)
}

class MedicationRepositoryImpl(
    private val medicationDao: MedicationDao
) : MedicationRepository {

    override suspend fun addMedication(medication: Medication) {
        medicationDao.insert(medication.toEntity())
    }

    override fun getMedications(userId: String): Flow<List<Medication>> {
        return medicationDao.getAllMedications(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateMedicationStatus(medicationId: String, isTaken: Boolean) {
        medicationDao.update(medicationId = medicationId, isTaken = isTaken)
    }

    private fun Medication.toEntity() = MedicationEntity(
        id = id,
        userId = userId,
        name = name,
        time = time,
        frequency = frequency,
        isTaken = isTaken,
        createdAt = createdAt,
        lastModified = lastModified
    )

    private fun MedicationEntity.toDomain() = Medication(
        id = id,
        userId = userId,
        name = name,
        time = time,
        frequency = frequency,
        isTaken = isTaken,
        createdAt = createdAt,
        lastModified = lastModified
    )

}