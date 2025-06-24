package com.raulespim.tictac.core.worker

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object WorkObserver {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun observeSyncWork(context: Context) {
        scope.launch {
            WorkManager.getInstance(context)
                .getWorkInfosForUniqueWorkFlow(SyncScheduler.SYNC_WORK_NAME)
                .collectLatest { workInfos ->
                    workInfos.forEach { workInfo ->
                        when (workInfo.state) {
                            WorkInfo.State.SUCCEEDED -> println("WorkObserver: Sincronização bem-sucedida")
                            WorkInfo.State.FAILED -> println("WorkObserver: Sincronização falhou")
                            WorkInfo.State.ENQUEUED -> println("WorkObserver: Sincronização enfileirada")
                            WorkInfo.State.RUNNING -> println("WorkObserver: Sincronização em execução")
                            else -> println("WorkObserver: Estado desconhecido: ${workInfo.state}")
                        }
                    }
                }
        }
    }
}