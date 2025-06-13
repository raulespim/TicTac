package com.raulespim.tictac.domain.model

data class Task(
    val id: String,
    val userId: String,
    val title: String,
    val description: String?,
    val time: Long, // Alert Timestamp
    val isCompleted: Boolean,
    val createdAt: Long
)
