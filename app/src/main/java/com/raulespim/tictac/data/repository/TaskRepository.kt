package com.raulespim.tictac.data.repository

import com.raulespim.tictac.data.local.dao.TaskDao
import com.raulespim.tictac.data.local.entity.TaskEntity
import com.raulespim.tictac.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface TaskRepository {
    suspend fun addTask(task: Task)
    fun getTasks(userId: String): Flow<List<Task>>
    suspend fun updateTaskStatus(taskId: String, isCompleted: Boolean)
}

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {

    override suspend fun addTask(task: Task) {
        taskDao.insert(task.toEntity())
    }

    override fun getTasks(userId: String): Flow<List<Task>> {
        return taskDao.getAllTasks(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun updateTaskStatus(taskId: String, isCompleted: Boolean) {
        taskDao.update(taskId = taskId, isCompleted = isCompleted)
    }

    private fun Task.toEntity() = TaskEntity(
        id = id,
        userId = userId,
        title = title,
        description = description,
        time = time,
        isCompleted = isCompleted,
        createdAt = createdAt
    )

    private fun TaskEntity.toDomain() = Task(
        id = id,
        userId = userId,
        title = title,
        description = description,
        time = time,
        isCompleted = isCompleted,
        createdAt = createdAt
    )
}