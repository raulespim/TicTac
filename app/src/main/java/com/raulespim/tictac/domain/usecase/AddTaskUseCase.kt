package com.raulespim.tictac.domain.usecase

import com.raulespim.tictac.data.repository.TaskRepository
import com.raulespim.tictac.domain.model.Task
import java.util.UUID

class AddTaskUseCase(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(
        userId: String,
        title: String,
        description: String?,
        time: Long
    ) {
        val task = Task(
            id = generateTaskId(),
            userId = userId,
            title = title,
            description = description,
            time = time,
            isCompleted = false,
            createdAt = System.currentTimeMillis()
        )
        taskRepository.addTask(task)
    }

    private fun generateTaskId(): String {
        return "TASK-${UUID.randomUUID().toString().take(8).uppercase()}"
    }
}