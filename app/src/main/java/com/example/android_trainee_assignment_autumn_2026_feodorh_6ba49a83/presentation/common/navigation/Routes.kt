package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.presentation.common.navigation

enum class Routes(val route: String) {
    NOTES("notes"),
    TASKS("tasks"),
    SETTINGS("settings"),
    EDIT_NOTE("edit_note/{noteId}");  // с параметром

    companion object {
        fun editNote(noteId: Long) = "edit_note/$noteId"
    }
}