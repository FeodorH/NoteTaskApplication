package com.example.note_task_app.presentation.common.navigation

enum class Routes(val route: String) {
    NOTES("notes"),
    TASKS("tasks"),
    SETTINGS("settings"),
    EDIT_NOTE("edit_note/{noteId}");  // с параметром

    companion object {
        fun editNote(noteId: Long) = "edit_note/$noteId"
    }
}