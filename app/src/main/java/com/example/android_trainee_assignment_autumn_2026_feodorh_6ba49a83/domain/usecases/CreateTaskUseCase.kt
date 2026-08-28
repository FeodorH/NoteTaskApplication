package com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.usecases

import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.model.Task
import com.example.android_trainee_assignment_autumn_2026_feodorh_6ba49a83.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task : Task) = repository.saveTask(task)
}