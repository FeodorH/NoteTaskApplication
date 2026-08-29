package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks

import android.Manifest
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.api.GigaChatApi
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.dto.BalanceResponse
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.dto.ChatRequest
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.dto.ChatResponse
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.dto.ModelsResponse
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.dto.TokenResponse
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.data.network.gigachat.service.GigaChatServiceImpl
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.AppColorScheme
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Task
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.ThemeMode
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository.TaskRepository
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.GigaChatService
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceEvent
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceInputService
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.CreateTaskUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.DeleteTaskUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetTasksFlowUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GigaChatGenerateTaskUsingByVoiceUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.UpdateTaskStatusUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.theme.AppTheme
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks.models.TaskFilter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun TasksScreen(
    navController: NavController,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    LocalContext.current
    val recordAudioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Задачи") },
                actions = {
                    IconButton(
                        onClick = {
                            if (state.isRecording) {
                                viewModel.cancelVoiceInput()
                            } else {
                                if (recordAudioPermissionState.status.isGranted) {
                                    viewModel.startVoiceInput()
                                } else {
                                    recordAudioPermissionState.launchPermissionRequest()
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (state.isRecording) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = if (state.isRecording) "Остановить запись" else "Голосовой ввод"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.startAddingTask() }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить задачу")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Строка поиска
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Поиск") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Фильтры
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskFilter.ALL.let { filter ->
                    FilterChip(
                        selected = state.filter == filter,
                        onClick = { viewModel.setFilter(filter) },
                        label = { Text("Все") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                TaskFilter.ACTIVE.let { filter ->
                    FilterChip(
                        selected = state.filter == filter,
                        onClick = { viewModel.setFilter(filter) },
                        label = { Text("Активные") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                TaskFilter.COMPLETED.let { filter ->
                    FilterChip(
                        selected = state.filter == filter,
                        onClick = { viewModel.setFilter(filter) },
                        label = { Text("Выполненные") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Состояния экрана
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                state.errorMessage != null -> {
                    Text(
                        text = state.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                state.tasks.isEmpty() && !state.isAddingMode -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Нет задач")
                    }
                }

                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (state.isAddingMode) {
                            item {
                                AddTaskItem(
                                    title = state.newTaskTitle,
                                    onTitleChange = { viewModel.updateNewTaskTitle(it) },
                                    onConfirm = { viewModel.confirmAddTask() },
                                    onCancel = { viewModel.cancelAddingTask() }
                                )
                            }
                        }
                        items(
                            items = state.tasks,
                            key = { it.id }
                        ) { task ->
                            TaskItem(
                                task = task,
                                onToggleCompletion = { isChecked ->
                                    viewModel.toggleTaskCompletion(task.id, isChecked)
                                },
                                onDelete = { viewModel.deleteTask(task.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggleCompletion: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = onToggleCompletion
            )
            Text(
                text = task.title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                fontWeight = FontWeight.Medium,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                color = if (task.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                else MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить")
            }
        }
    }
}

@Composable
fun AddTaskItem(
    title: String,
    onTitleChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = false, onCheckedChange = null, enabled = false)
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                placeholder = { Text("Введите название задачи") },
                singleLine = true,
                isError = title.isBlank() // можно добавить проверку при подтверждении
            )
            IconButton(onClick = onConfirm) {
                Icon(Icons.Default.Check, contentDescription = "Сохранить")
            }
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Close, contentDescription = "Отменить")
            }
        }
    }
}
