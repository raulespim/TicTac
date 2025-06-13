package com.raulespim.tictac.ui.screens.task.viewactions

sealed class TaskIntent {
    data object OnAddTaskClicked : TaskIntent()
    data object OnDismissAddDialog : TaskIntent()
    data class OnSaveTask(val title: String, val description: String?, val time: Long) : TaskIntent()
    data class OnTaskStatusChanged(val taskId: String, val isCompleted: Boolean) : TaskIntent()
}

sealed class TaskEffect {

}