package com.raulespim.tictac.core.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.raulespim.tictac.data.repository.AuthRepository
import com.raulespim.tictac.domain.usecase.SyncMedicationsUseCase
import com.raulespim.tictac.domain.usecase.SyncTasksUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val authRepository: AuthRepository by inject()
    private val syncMedicationsUseCase: SyncMedicationsUseCase by inject()
    private val syncTasksUseCase: SyncTasksUseCase by inject()

    override suspend fun doWork(): Result {
        val userId = authRepository.getCurrentUserId() ?: return Result.failure()
        return try {
            syncMedicationsUseCase.invoke(userId)
            syncTasksUseCase.invoke(userId)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}