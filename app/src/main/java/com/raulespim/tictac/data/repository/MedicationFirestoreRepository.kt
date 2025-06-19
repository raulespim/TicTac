package com.raulespim.tictac.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.raulespim.tictac.core.util.Constants
import com.raulespim.tictac.domain.model.Medication
import kotlinx.coroutines.tasks.await

interface MedicationFirestoreRepository {
    suspend fun syncMedications(userId: String, localMedications: List<Medication>): List<Medication>
}

class MedicationFirestoreRepositoryImpl(
    private val firestore: FirebaseFirestore
) : MedicationFirestoreRepository {

    override suspend fun syncMedications(
        userId: String,
        localMedications: List<Medication>
    ): List<Medication> {

        val collection = firestore.collection(Constants.USERS_COLLECTION).document(userId).collection(Constants.MEDICATIONS_COLLECTION)
        val remoteMedications = collection.get().await().documents.mapNotNull { doc ->
            doc.toObject(Medication::class.java)?.copy(id = doc.id)
        }

        // Resolver conflitos: manter a versão mais recente com base em lastModified
        val mergedMedications = mutableListOf<Medication>()
        val localMap = localMedications.associateBy { it.id }
        val remoteMap = remoteMedications.associateBy { it.id }

        // Processar todos os IDs únicos
        val allIds = localMap.keys + remoteMap.keys
        for (id in allIds) {
            val local = localMap[id]
            val remote = remoteMap[id]

            when {
                local != null && remote == null -> {
                    // Apenas local: enviar para Firestore
                    collection.document(id).set(local).await()
                    mergedMedications.add(local)
                }
                local == null && remote != null -> {
                    // Apenas remoto: adicionar à lista
                    mergedMedications.add(remote)
                }
                local != null && remote != null -> {
                    // Ambos existem: escolher o mais recente
                    val latest = if (local.lastModified >= remote.lastModified) local else remote
                    if (latest == local) {
                        collection.document(id).set(local).await()
                    }
                    mergedMedications.add(latest)
                }
            }
        }

        return mergedMedications
    }

}