package com.raulespim.tictac.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val description: String?,
    val time: Long, // Alert Timestamp
    val isCompleted: Boolean,
    val createdAt: Long,
    val lastModified: Long
)
