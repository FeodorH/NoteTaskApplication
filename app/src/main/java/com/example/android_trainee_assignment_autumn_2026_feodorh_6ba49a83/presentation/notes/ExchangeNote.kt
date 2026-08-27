package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.R
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models.ExchangeNoteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.SavedStateHandle
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository.NoteRepository
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.CreateNoteUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetNoteByIdUseCase
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ExchangeNote(
    navController: NavController,
    viewModel: ExchangeNoteViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Запрос разрешений
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val readImagesPermissionState = rememberPermissionState(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    // Временный файл для камеры
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // Галерея
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.updateImageUri(it.toString())
        }
    }

    // Камера
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraUri != null) {
            viewModel.updateImageUri(tempCameraUri.toString())
        } else {
            // Если съёмка не удалась, удаляем временный файл
            tempCameraUri?.let { uri ->
                try {
                    File(uri.path ?: "").delete()
                } catch (_: Exception) { }
            }
        }
        tempCameraUri = null
    }

    // Функция для создания временного файла
    fun createTempImageUri(): Uri? {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "JPEG_${timestamp}_"
        return try {
            val storageDir = context.cacheDir
            val file = File.createTempFile(fileName, ".jpg", storageDir)
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            null
        }
    }

    @Composable
    fun ErrorMessage(error: String?){
        Text(
            text = error ?: "Неизвестная ошибка",
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }


    // Автовыход при сохранении
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navController.popBackStack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TopAppBar(
                title = {
                    Text(if (viewModel.noteId == 0L) "Новая заметка" else "Редактирование")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    // Кнопка голосового ввода (будет позже)
                    IconButton(onClick = { /* TODO: голосовой ввод */ }) {
                        Icon(Icons.Default.Mic, contentDescription = "Голосовой ввод")
                    }
                }
            )

            // Поле заголовка
            OutlinedTextField(
                value = state.title,
                onValueChange = {
                    viewModel.updateTitle(it)
                    viewModel.clearError()
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Заголовок") },
                singleLine = true,
                isError = state.title.isBlank() && state.errorMessage != null,
                enabled = !state.isLoading && !state.isSaving
            )

            // Поле содержимого
            OutlinedTextField(
                value = state.content,
                onValueChange = {
                    viewModel.updateContent(it)
                    viewModel.clearError()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                label = { Text("Текст заметки") },
                enabled = !state.isLoading && !state.isSaving
            )

            // Блок работы с изображением
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Изображение",
                        style = MaterialTheme.typography.labelMedium
                    )

                    if (state.imageUri != null) {
                        // Превью изображения
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = Uri.parse(state.imageUri),
                                    error = painterResource(R.drawable.ic_error)
                                ),
                                contentDescription = "Превью изображения",
                                modifier = Modifier.fillMaxSize()
                            )

                            // Кнопка удаления изображения
                            IconButton(
                                onClick = { viewModel.removeImage() },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Удалить изображение",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (readImagesPermissionState.status.isGranted) {
                                        galleryLauncher.launch("image/*")
                                    } else {
                                        readImagesPermissionState.launchPermissionRequest()
                                    }
                                },
                                enabled = !state.isSaving
                            ) {
                                Icon(Icons.Default.Image, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Из галереи")
                            }

                            Button(
                                onClick = {
                                    if (cameraPermissionState.status.isGranted) {
                                        tempCameraUri = createTempImageUri()
                                        tempCameraUri?.let { uri ->
                                            cameraLauncher.launch(uri)
                                        }
                                    } else {
                                        cameraPermissionState.launchPermissionRequest()
                                    }
                                },
                                enabled = !state.isSaving
                            ) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Камера")
                            }
                        }
                        if (!readImagesPermissionState.status.isGranted && readImagesPermissionState.status.shouldShowRationale) {
                            ErrorMessage("Для выбора изображения из галереи нужно разрешение.")
                        }
                        if (!cameraPermissionState.status.isGranted && cameraPermissionState.status.shouldShowRationale) {
                            ErrorMessage("Для выбора изображения из галереи нужно разрешение.")
                        }
                    }
                }
            }

            if (state.errorMessage != null) {
                ErrorMessage(error = state.errorMessage)
            }
        }
        FloatingActionButton(
            onClick = {
                if (!state.isSaving) {
                    viewModel.saveNote()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .alpha(if (state.isSaving) 0.5f else 1f)
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Icon(Icons.Default.Check, contentDescription = "Сохранить")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewExchangeNote() {
    val fakeRepository = object : NoteRepository {
        override fun getNotesFlow(): Flow<List<Note>> = flowOf(emptyList())
        override suspend fun getNoteById(id: Long): Note? = null
        override suspend fun saveNote(note: Note) { }
        override suspend fun deleteNote(id: Long) { }
        override suspend fun deleteAllNotes() { }
    }

    val fakeGetNoteById = GetNoteByIdUseCase(fakeRepository)
    val fakeCreateNote = CreateNoteUseCase(fakeRepository)

    val fakeViewModel = remember {
        object : ExchangeNoteViewModel(
            getNoteById = fakeGetNoteById,
            createNote = fakeCreateNote,
            savedStateHandle = SavedStateHandle()
        ) {
            override val state = MutableStateFlow(
                ExchangeNoteUiState(
                    title = "Тестовая заметка",
                    content = "Это содержимое заметки для превью",
                    imageUri = null
                )
            ).asStateFlow()
        }
    }

    MaterialTheme {
        ExchangeNote(
            navController = rememberNavController(),
            viewModel = fakeViewModel
        )
    }
}