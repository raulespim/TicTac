package com.raulespim.tictac.domain.model

data class Medication(
    val id: String,
    val userId: String,
    val name: String,
    val time: Long, // Alert Timestamp
    val frequency: String, // e.g., "DAILY", "WEEKLY"
    val isTaken: Boolean,
    val createdAt: Long,
    val lastModified: Long
) {
    // Construtor sem argumentos exigido pelo Firestore
    constructor() : this("", "", "", 0L, "", false, 0L, 0L)
}
