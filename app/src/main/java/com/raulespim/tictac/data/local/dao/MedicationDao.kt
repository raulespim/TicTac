package com.raulespim.tictac.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.raulespim.tictac.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medication: MedicationEntity)

    @Query("UPDATE medications SET isTaken = :isTaken WHERE id = :medicationId")
    suspend fun update(medicationId: String, isTaken: Boolean)

    @Query("DELETE FROM medications WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM medications WHERE userId = :userId ORDER BY time ASC")
    fun getAllMedications(userId: String): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE id = :medicationId")
    fun getMedication(medicationId: String): Flow<List<MedicationEntity>>
}