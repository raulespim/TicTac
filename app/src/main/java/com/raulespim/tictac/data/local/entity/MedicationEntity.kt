package com.raulespim.tictac.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val time: Long, // Alert Timestamp
    val frequency: String, // e.g., "DAILY", "WEEKLY"
    val isTaken: Boolean,
    val createdAt: Long
)
