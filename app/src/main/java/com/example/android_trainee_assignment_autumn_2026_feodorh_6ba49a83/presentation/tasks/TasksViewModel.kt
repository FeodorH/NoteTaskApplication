package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Task
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.CreateTaskUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.DeleteTaskUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetTasksFlowUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.UpdateTaskStatusUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks.models.TaskFilter
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks.models.TasksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksFlow: GetTasksFlowUseCase,
    private val saveTask: CreateTaskUseCase,
    private val deleteTaskById: DeleteTaskUseCase,
    private val updateTaskStatus: UpdateTaskStatusUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<TasksUiState>(TasksUiState.Loading)
    val state: StateFlow<TasksUiState> = _state.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")
    private val filterFlow = MutableStateFlow(TaskFilter.ALL)

    init {
        viewModelScope.launch {
            combine(
                getTasksFlow(),
                searchQueryFlow,
                filterFlow
            ) { tasks, query, filter ->
                applyFiltersAndSort(tasks, query, filter)
            }.collect { filteredTasks ->
                _state.value = TasksUiState.Success(
                    tasks = filteredTasks,
                    searchQuery = searchQueryFlow.value,
                    filter = filterFlow.value,
                    isAddingMode = (_state.value as? TasksUiState.Success)?.isAddingMode ?: false,
                    newTaskTitle = (_state.value as? TasksUiState.Success)?.newTaskTitle ?: ""
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQueryFlow.value = query
    }

    fun setFilter(filter: TaskFilter) {
        filterFlow.value = filter
    }

    fun startAddingTask() {
        val currentState = _state.value as? TasksUiState.Success ?: return
        _state.value = currentState.copy(
            isAddingMode = true,
            newTaskTitle = ""
        )
    }

    fun updateNewTaskTitle(title: String) {
        val currentState = _state.value as? TasksUiState.Success ?: return
        _state.value = currentState.copy(newTaskTitle = title)
    }

    fun confirmAddTask() {
        val currentState = _state.value as? TasksUiState.Success ?: return
        val title = currentState.newTaskTitle.trim()
        if (title.isEmpty()) {
            _state.value = currentState.copy(errorMessage = "Название не может быть пустым")
            return
        }
        viewModelScope.launch {
            saveTask(Task(title = title))
            _state.value = (state.value as TasksUiState.Success).copy(
                isAddingMode = false,
                newTaskTitle = ""
            )
        }
    }

    fun cancelAddingTask() {
        val currentState = _state.value as? TasksUiState.Success ?: return
        _state.value = currentState.copy(
            isAddingMode = false,
            newTaskTitle = ""
        )
    }

    fun toggleTaskCompletion(taskId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            updateTaskStatus(taskId, isCompleted)
        }
    }

    fun deleteTask(taskId: Long) {
        viewModelScope.launch {
            deleteTaskById(taskId)
        }
    }

    fun clearError() {
        val currentState = _state.value as? TasksUiState.Success ?: return
        _state.value = currentState.copy(errorMessage = null)
    }

    private fun applyFiltersAndSort(
        tasks: List<Task>,
        query: String,
        filter: TaskFilter
    ): List<Task> {
        val filteredByStatus = when (filter) {
            TaskFilter.ALL -> tasks
            TaskFilter.ACTIVE -> tasks.filter { !it.isCompleted }
            TaskFilter.COMPLETED -> tasks.filter { it.isCompleted }
        }

        val filteredByQuery = if (query.isBlank()) {
            filteredByStatus
        } else {
            filteredByStatus.filter { it.title.contains(query, ignoreCase = true) }
        }

        // Деление на группы
        val active = filteredByQuery.filter { !it.isCompleted }
        val completed = filteredByQuery.filter { it.isCompleted }

        val sortedActive = active.sortedByDescending { it.createdAt }
        val sortedCompleted = completed.sortedByDescending { it.createdAt }

        return sortedActive + sortedCompleted
    }
}