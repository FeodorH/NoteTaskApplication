package com.example.note_task_app.domain.usecases

import android.content.Context
import android.net.Uri
import com.example.note_task_app.domain.model.Note
import com.example.note_task_app.domain.repository.NoteRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class CreateNoteUseCase @Inject constructor(
    private val repository: NoteRepository,
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(note: Note): Note = withContext(Dispatchers.IO) {
        // Копируем изображение, если это внешний URI
        val finalNote = note.copy(
            imageUri = copyImageIfNeeded(note.imageUri)
        )
        repository.saveNote(finalNote)
        finalNote
    }

    private fun copyImageIfNeeded(uriString: String?): String? {
        if (uriString == null) return null
        if (uriString.startsWith("/")) return uriString

        val uri = Uri.parse(uriString)
        if (uri.scheme == "content" || uri.scheme == "file") {
            return try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return null
                val fileName = "img_${System.currentTimeMillis()}.jpg"
                val destFile = File(context.filesDir, fileName)
                destFile.outputStream().use { output ->
                    inputStream.copyTo(output)
                }
                destFile.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        return uriString
    }
}