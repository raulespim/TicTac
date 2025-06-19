package com.raulespim.tictac.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.raulespim.tictac.core.util.Constants
import com.raulespim.tictac.domain.model.Task
import kotlinx.coroutines.tasks.await

interface TaskFirestoreRepository {
    suspend fun syncTasks(userId: String, localTasks: List<Task>): List<Task>
}

class TaskFirestoreRepositoryImpl(
    private val firestore: FirebaseFirestore
) : TaskFirestoreRepository {

    override suspend fun syncTasks(userId: String, localTasks: List<Task>): List<Task> {
        val collection = firestore.collection(Constants.USERS_COLLECTION).document(userId).collection(Constants.TASKS_COLLECTION)
        val remoteTasks = collection.get().await().documents.mapNotNull { doc ->
            doc.toObject(Task::class.java)?.copy(id = doc.id)
        }

        // Resolver conflitos: manter a versão mais recente com base em updatedAt
        val mergedTasks = mutableListOf<Task>()
        val localMap = localTasks.associateBy { it.id }
        val remoteMap = remoteTasks.associateBy { it.id }

        // Processar todos os IDs únicos
        val allIds = localMap.keys + remoteMap.keys
        for (id in allIds) {
            val local = localMap[id]
            val remote = remoteMap[id]

            when {
                local != null && remote == null -> {
                    // Apenas local: enviar para Firestore
                    collection.document(id).set(local).await()
                    mergedTasks.add(local)
                }
                local == null && remote != null -> {
                    // Apenas remoto: adicionar à lista
                    mergedTasks.add(remote)
                }
                local != null && remote != null -> {
                    // Ambos existem: escolher o mais recente
                    val latest = if (local.lastModified >= remote.lastModified) local else remote
                    if (latest == local) {
                        collection.document(id).set(local).await()
                    }
                    mergedTasks.add(latest)
                }
            }
        }

        return mergedTasks
    }
}