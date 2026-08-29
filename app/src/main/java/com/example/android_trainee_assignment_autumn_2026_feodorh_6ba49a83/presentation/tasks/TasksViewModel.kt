package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Task
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceEvent
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceInputService
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.CreateTaskUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.DeleteTaskUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetTasksFlowUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GigaChatGenerateTaskUsingByVoiceUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.UpdateTaskStatusUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks.models.TaskFilter
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks.models.TasksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksFlow: GetTasksFlowUseCase,
    private val saveTask: CreateTaskUseCase,
    private val deleteTaskById: DeleteTaskUseCase,
    private val updateTaskStatus: UpdateTaskStatusUseCase,
    private val voiceInputService: VoiceInputService,
    private val generateTaskFromVoice: GigaChatGenerateTaskUsingByVoiceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TasksUiState())
    val state: StateFlow<TasksUiState> = _state.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")
    private val filterFlow = MutableStateFlow(TaskFilter.ALL)

    private var voiceJob: Job? = null

    init {
        viewModelScope.launch {
            combine(
                getTasksFlow(),
                searchQueryFlow,
                filterFlow
            ) { tasks, query, filter ->
                applyFiltersAndSort(tasks, query, filter)
            }.collect { filteredTasks ->
                _state.update { currentState ->
                    currentState.copy(
                        tasks = filteredTasks,
                        searchQuery = searchQueryFlow.value,
                        filter = filterFlow.value
                    )
                }
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
        _state.update { it.copy(isAddingMode = true, newTaskTitle = "") }
    }

    fun updateNewTaskTitle(title: String) {
        _state.update { it.copy(newTaskTitle = title) }
    }

    fun confirmAddTask() {
        val currentState = _state.value
        val title = currentState.newTaskTitle.trim()
        if (title.isEmpty()) {
            _state.update { it.copy(errorMessage = "Название не может быть пустым") }
            return
        }
        viewModelScope.launch {
            saveTask(Task(title = title))
            _state.update { it.copy(isAddingMode = false, newTaskTitle = "") }
        }
    }

    fun cancelAddingTask() {
        _state.update { it.copy(isAddingMode = false, newTaskTitle = "") }
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

    fun startVoiceInput() {
        if (voiceJob?.isActive == true) return

        _state.update { it.copy(isRecording = true, errorMessage = null) }

        voiceJob = viewModelScope.launch {
            voiceInputService.startListening().collect { event ->
                when (event) {
                    is VoiceEvent.Ready -> {  }
                    is VoiceEvent.Listening -> {
                        _state.update { it.copy(isRecording = true) }
                    }
                    is VoiceEvent.PartialResult -> { }
                    is VoiceEvent.FinalResult -> {
                        _state.update { it.copy(isRecording = false) }
                        // Передаём распознанный текст в метод создания задачи
                        createTaskFromVoice(event.text)
                    }
                    is VoiceEvent.Error -> {
                        _state.update {
                            it.copy(
                                isRecording = false,
                                errorMessage = event.message
                            )
                        }
                    }
                    is VoiceEvent.Cancelled -> {
                        _state.update { it.copy(isRecording = false) }
                    }
                }
            }
        }
    }

    fun cancelVoiceInput() {
        voiceInputService.stopListening()
        voiceJob?.cancel()
        voiceJob = null
        _state.update { it.copy(isRecording = false) }
    }

    fun createTaskFromVoice(voiceText: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val taskTitle = generateTaskFromVoice(voiceText)
                if (!taskTitle.isNullOrBlank()) {
                    saveTask(Task(title = taskTitle))
                } else {
                    _state.update { it.copy(errorMessage = "Не удалось сгенерировать задачу") }
                }
            } catch (e: NullPointerException) {//TODO
                _state.update { it.copy(errorMessage = e.message ?: "Ошибка генерации") }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
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

        val active = filteredByQuery.filter { !it.isCompleted }
        val completed = filteredByQuery.filter { it.isCompleted }

        return active.sortedByDescending { it.createdAt } +
                completed.sortedByDescending { it.createdAt }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

    override fun onCleared() {
        super.onCleared()
        voiceJob?.cancel()
        voiceInputService.stopListening()
    }
}