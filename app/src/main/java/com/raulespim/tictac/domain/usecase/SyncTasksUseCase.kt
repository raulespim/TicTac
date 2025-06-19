package com.raulespim.tictac.domain.usecase

import com.raulespim.tictac.data.repository.TaskFirestoreRepository
import com.raulespim.tictac.data.repository.TaskRepository
import kotlinx.coroutines.flow.first

class SyncTasksUseCase(
    private val localRepository: TaskRepository,
    private val firestoreRepository: TaskFirestoreRepository
) {
    suspend operator fun invoke(userId: String) {
        // Obter tarefas locais
        val localTasks = localRepository.getTasks(userId).first()

        // Sincronizar com Firestore
        val mergedTasks = firestoreRepository.syncTasks(userId, localTasks)

        // Atualizar Room com os dados sincronizados
        mergedTasks.forEach { task ->
            localRepository.addTask(task)
        }
    }
}