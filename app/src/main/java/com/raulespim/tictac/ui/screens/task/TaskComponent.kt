package com.raulespim.tictac.ui.screens.task

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.raulespim.tictac.domain.usecase.AddTaskUseCase
import com.raulespim.tictac.domain.usecase.GetTasksUseCase
import com.raulespim.tictac.domain.usecase.UpdateTaskStatusUseCase
import com.raulespim.tictac.ui.screens.task.viewactions.TaskEffect
import com.raulespim.tictac.ui.screens.task.viewactions.TaskIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface TaskComponent {
    val state: StateFlow<TaskState>
    val effects: Flow<TaskEffect>
    fun onIntent(intent: TaskIntent)
}

class TaskComponentImpl(
    componentContext: ComponentContext,
    private val addTaskUseCase: AddTaskUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val userId: String
) : TaskComponent, ComponentContext by componentContext {

    private val _state = MutableStateFlow(TaskState())
    override val state: StateFlow<TaskState> = _state.asStateFlow()

    private val _effects = Channel<TaskEffect>(Channel.BUFFERED)
    override val effects: Flow<TaskEffect> = _effects.receiveAsFlow()

    private val scope = coroutineScope()

    init {
        scope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            getTasksUseCase.invoke(userId).onEach { tasks ->
                _state.update { it.copy(tasks = tasks, isLoading = false) }
            }
        }
    }

    override fun onIntent(intent: TaskIntent) {
        when (intent) {
            TaskIntent.OnAddTaskClicked -> _state.update { it.copy(showAddDialog = true) }
            TaskIntent.OnDismissAddDialog -> _state.update { it.copy(showAddDialog = false) }
            is TaskIntent.OnSaveTask -> onSaveTask(intent.title, intent.description, intent.time)
            is TaskIntent.OnTaskStatusChanged -> onTaskStatusChanged(intent.taskId, intent.isCompleted)
        }
    }

    private fun onSaveTask(title: String, description: String?, time: Long) {
        scope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                addTaskUseCase.invoke(userId, title, description, time)
                _state.update { it.copy(showAddDialog = false, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun onTaskStatusChanged(taskId: String, isCompleted: Boolean) {
        scope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                updateTaskStatusUseCase.invoke(taskId, isCompleted)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

}