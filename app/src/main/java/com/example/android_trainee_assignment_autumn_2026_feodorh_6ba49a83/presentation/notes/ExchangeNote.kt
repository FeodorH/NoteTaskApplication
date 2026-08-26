package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes

import android.net.Uri
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.R
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models.ExchangeNoteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.SavedStateHandle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeNote(
    navController: NavController,
    viewModel: ExchangeNoteViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Автовыход при сохранении
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navController.popBackStack()
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            // TODO: показать Snackbar или диалог
        }
    }

    Scaffold(
        topBar = {
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (!state.isSaving) {
                        viewModel.saveNote()
                    }
                },
                modifier = Modifier.alpha(if (state.isSaving) 0.5f else 1f)
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Check, contentDescription = "Сохранить")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Поле заголовка
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.updateTitle(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Заголовок") },
                singleLine = true,
                isError = state.title.isBlank() && state.errorMessage != null,
                enabled = !state.isLoading && !state.isSaving
            )

            // Поле содержимого
            OutlinedTextField(
                value = state.content,
                onValueChange = { viewModel.updateContent(it) },
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
                                onClick = { /* TODO: открыть галерею */ },
                                enabled = !state.isSaving
                            ) {
                                Icon(Icons.Default.Image, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Из галереи")
                            }

                            Button(
                                onClick = { /* TODO: открыть камеру */ },
                                enabled = !state.isSaving
                            ) {
                                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Камера")
                            }
                        }
                    }
                }
            }

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage ?: "Неожиданная ошибка",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewExchangeNote() {
    val fakeViewModel = remember {
        object : ExchangeNoteViewModel(SavedStateHandle()) {
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
            viewModel = fakeViewModel  // передаём мок
        )
    }
}