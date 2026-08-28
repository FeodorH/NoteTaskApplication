package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Task
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository.TaskRepository
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.CreateTaskUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.DeleteTaskUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetTasksFlowUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.UpdateTaskStatusUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks.models.TaskFilter
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.tasks.models.TasksUiState
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.ui.theme.Androidtraineeassignmentautumn2026feodorh6ba49a83Theme
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Tasks(
    navController: NavController,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Задачи") },
                actions = {
                    IconButton(onClick = { /* TODO: голосовое создание задачи */ }) {
                        Icon(Icons.Default.Mic, contentDescription = "Голосовой ввод")
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
        when (state) {
            is TasksUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is TasksUiState.Success -> {
                val successState = state as TasksUiState.Success
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    // Строка поиска
                    OutlinedTextField(
                        value = successState.searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Поиск") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Фильтр по статусу (chip-ы)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TaskFilter.ALL.let { filter ->
                            FilterChip(
                                selected = successState.filter == filter,
                                onClick = { viewModel.setFilter(filter) },
                                label = { Text("Все") }
                            )
                        }
                        TaskFilter.ACTIVE.let { filter ->
                            FilterChip(
                                selected = successState.filter == filter,
                                onClick = { viewModel.setFilter(filter) },
                                label = { Text("Активные") }
                            )
                        }
                        TaskFilter.COMPLETED.let { filter ->
                            FilterChip(
                                selected = successState.filter == filter,
                                onClick = { viewModel.setFilter(filter) },
                                label = { Text("Выполненные") }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Список задач
                    if (successState.tasks.isEmpty() && !successState.isAddingMode) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Нет задач")
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Если режим добавления активен, показываем временный элемент
                            if (successState.isAddingMode) {
                                item {
                                    AddTaskItem(
                                        title = successState.newTaskTitle,
                                        onTitleChange = { viewModel.updateNewTaskTitle(it) },
                                        onConfirm = { viewModel.confirmAddTask() },
                                        onCancel = { viewModel.cancelAddingTask() }
                                    )
                                }
                            }
                            items(
                                items = successState.tasks,
                                key = { it.id }
                            ) { task ->
                                TaskItem(
                                    task = task,
                                    onToggleCompletion = { isChecked ->
                                        viewModel.toggleTaskCompletion(task.id, isChecked)
                                    },
                                    onDelete = {
                                        viewModel.deleteTask(task.id)
                                    }
                                )
                            }
                        }
                    }

                    successState.errorMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
            is TasksUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Ошибка загрузки задач")
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
                modifier = Modifier.weight(1f).padding(start = 8.dp),
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
                modifier = Modifier.weight(1f).padding(start = 8.dp),
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

// Фабрика для превью
fun previewTasksViewModel(): TasksViewModel {
    // Создаём заглушки UseCase
    val fakeRepository = object : TaskRepository {
        override fun getTasksFlow() = emptyFlow<List<Task>>()
        override suspend fun saveTask(task: Task) {}
        override suspend fun deleteTask(id: Long) {}
        override suspend fun updateTaskStatus(id: Long, isCompleted: Boolean) {}
    }
    val getTasksFlow = GetTasksFlowUseCase(fakeRepository)
    val saveTask = CreateTaskUseCase(fakeRepository)
    val deleteTask = DeleteTaskUseCase(fakeRepository)
    val updateTaskStatus = UpdateTaskStatusUseCase(fakeRepository)
    return TasksViewModel(getTasksFlow, saveTask, deleteTask, updateTaskStatus)
}

@Preview
@Composable
fun PreviewTasksScreen() {
    Androidtraineeassignmentautumn2026feodorh6ba49a83Theme {
        Tasks(
            navController = rememberNavController(),
            viewModel = previewTasksViewModel()
        )
    }
}

