package com.raulespim.tictac.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.raulespim.tictac.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Query("UPDATE tasks SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun update(taskId: String, isCompleted: Boolean)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM tasks WHERE userId = :userId ORDER BY time ASC")
    fun getAllTasks(userId: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTask(taskId: String): Flow<List<TaskEntity>>
}