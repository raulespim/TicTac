package com.raulespim.tictac.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raulespim.tictac.data.local.dao.MedicationDao
import com.raulespim.tictac.data.local.dao.TaskDao
import com.raulespim.tictac.data.local.entity.MedicationEntity
import com.raulespim.tictac.data.local.entity.TaskEntity

@Database(entities = [MedicationEntity::class, TaskEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun taskDao(): TaskDao
}