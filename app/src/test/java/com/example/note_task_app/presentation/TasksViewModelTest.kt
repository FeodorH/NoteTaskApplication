package com.example.note_task_app.presentation

import com.example.note_task_app.data.service.VoskVoiceInputService
import com.example.note_task_app.domain.model.Task
import com.example.note_task_app.domain.usecases.*
import com.example.note_task_app.presentation.tasks.TasksViewModel
import com.example.note_task_app.presentation.tasks.models.TaskFilter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TasksViewModelTest {

    private lateinit var viewModel: TasksViewModel
    private val getTasksFlow: GetTasksFlowUseCase = mockk()
    private val saveTask: CreateTaskUseCase = mockk()
    private val deleteTask: DeleteTaskUseCase = mockk()
    private val updateTaskStatus: UpdateTaskStatusUseCase = mockk()
    private val voiceInputService: VoskVoiceInputService = mockk()
    private val generateTaskUsingByVoiceUseCase: GigaChatGenerateTaskUsingByVoiceUseCase = mockk()

    private val tasksFlow = MutableStateFlow<List<Task>>(emptyList())
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val tasks = listOf(
            Task(id = 1, title = "Task 1", isCompleted = false, createdAt = 1000),
            Task(id = 2, title = "Task 2", isCompleted = true, createdAt = 2000),
            Task(id = 3, title = "Task 3", isCompleted = false, createdAt = 3000)
        )
        tasksFlow.value = tasks
        coEvery { getTasksFlow() } returns tasksFlow
        coEvery { saveTask(any()) } returns Unit
        coEvery { deleteTask(any()) } returns Unit
        coEvery { updateTaskStatus(any(), any()) } returns Unit
        coEvery { voiceInputService.startListening() } returns flowOf()
        coEvery { generateTaskUsingByVoiceUseCase(any()) } returns "Generated task"

        viewModel = TasksViewModel(
            getTasksFlow = getTasksFlow,
            saveTask = saveTask,
            deleteTaskById = deleteTask,
            updateTaskStatus = updateTaskStatus,
            voiceInputService = voiceInputService,
            generateTaskFromVoice = generateTaskUsingByVoiceUseCase
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setFilter should filter tasks by status`() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setFilter(TaskFilter.ACTIVE)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals(2, state.tasks.size)
        state.tasks.forEach { task ->
            assertEquals(false, task.isCompleted)
        }
    }

    @Test
    fun `toggleTaskCompletion should call updateTaskStatus UseCase`() = runTest(testDispatcher) {
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.toggleTaskCompletion(1L, true)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { updateTaskStatus(1L, true) }
    }
}
