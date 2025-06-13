package com.raulespim.tictac.ui.screens.task

import com.raulespim.tictac.domain.model.Task

data class TaskState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showAddDialog: Boolean = false
)