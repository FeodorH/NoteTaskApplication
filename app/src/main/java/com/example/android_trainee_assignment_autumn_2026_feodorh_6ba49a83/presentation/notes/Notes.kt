package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Note
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository.NoteRepository
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceEvent
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.service.VoiceInputService
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.CreateNoteUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases.GetNoteByIdUseCase
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation.Routes
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.notes.models.SortOrder
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.ui.theme.Androidtraineeassignmentautumn2026feodorh6ba49a83Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Notes(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Загружаем заметки при первом появлении
    LaunchedEffect(Unit) {
        viewModel.loadNotes()
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Заметки") },
                actions = {
                    IconButton(onClick = { viewModel.toggleSortOrder() }) {
                        Icon(
                            imageVector = if (state.sortOrder == SortOrder.NEWEST)
                                Icons.Default.Sort else Icons.Default.SortByAlpha,
                            contentDescription = "Сортировка"
                        )
                    }
                    IconButton(onClick = { viewModel.toggleDeleteMode() }) {
                        Icon(
                            imageVector = if (state.isDeleteMode)
                                Icons.Default.DeleteSweep else Icons.Default.Delete,
                            contentDescription = "Режим удаления"
                        )
                    }
                }
            )

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Поиск") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true
            )

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (state.notes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Нет заметок")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.notes,
                        key = { it.id }
                    ) { note ->
                        NoteItem(
                            note = note,
                            isDeleteMode = state.isDeleteMode,
                            onDelete = { viewModel.deleteNote(it) },
                            onClick = {
                                if (!state.isDeleteMode) {
                                    navController.navigate(Routes.editNote(note.id))
                                }
                            }
                        )
                    }
                }
            }
        }
        FloatingActionButton(
            onClick = { navController.navigate(Routes.editNote(0L)) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Создать заметку")
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    isDeleteMode: Boolean,
    onDelete: (Long) -> Unit,
    onClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !isDeleteMode) { onClick(note.id) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Превью (заглушка или изображение)
            if (note.imageUri != null) {
                Image(
                    painter = rememberImagePainter(note.imageUri),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Image, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = note.content ?: "Без текста",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = formatDate(note.createdAt),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (isDeleteMode) {
                IconButton(onClick = { onDelete(note.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}

// Фабрика для превью
fun previewExchangeNoteViewModel(
    noteId: Long = 1L,
    existingNote: Note? = null
): ExchangeNoteViewModel {
    val fakeNote = existingNote ?: Note(
        id = noteId,
        title = "Тестовая заметка",
        content = "Это содержимое заметки для превью.",
        imageUri = null,
        createdAt = System.currentTimeMillis()
    )

    val fakeRepository = object : NoteRepository {
        override fun getNotesFlow(): Flow<List<Note>> = flowOf(listOf(fakeNote))
        override suspend fun getNoteById(id: Long): Note? =
            if (id == fakeNote.id) fakeNote else null
        override suspend fun saveNote(note: Note) { /* ничего */ }
        override suspend fun deleteNote(id: Long) { /* ничего */ }
        override suspend fun deleteAllNotes() { /* ничего */ }
    }

    val fakeVoiceService = object : VoiceInputService {
        override fun startListening(): Flow<VoiceEvent> = emptyFlow()
        override fun stopListening() { /* ничего */ }
    }

    val getNoteById = GetNoteByIdUseCase(fakeRepository)
    val createNote = CreateNoteUseCase(fakeRepository)

    val savedStateHandle = SavedStateHandle().apply {
        set("noteId", noteId)
    }

    return ExchangeNoteViewModel(
        getNoteById = getNoteById,
        createNote = createNote,
        voiceInputService = fakeVoiceService,
        savedStateHandle = savedStateHandle
    )
}

@Preview
@Composable
private fun PreviewExchangeNote() {
    Androidtraineeassignmentautumn2026feodorh6ba49a83Theme {
        ExchangeNote(
            navController = rememberNavController(),
            viewModel = previewExchangeNoteViewModel(noteId = 1L)
        )
    }
}