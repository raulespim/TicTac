package com.raulespim.tictac.domain.usecase

import com.raulespim.tictac.data.repository.TaskRepository

class UpdateTaskStatusUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: String, isCompleted: Boolean) {
        taskRepository.updateTaskStatus(taskId, isCompleted)
    }
}