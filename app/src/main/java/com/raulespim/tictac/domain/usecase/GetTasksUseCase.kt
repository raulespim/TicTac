package com.raulespim.tictac.domain.usecase

import com.raulespim.tictac.data.repository.TaskRepository
import com.raulespim.tictac.domain.model.Task
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(userId: String): Flow<List<Task>> {
        return taskRepository.getTasks(userId)
    }
}