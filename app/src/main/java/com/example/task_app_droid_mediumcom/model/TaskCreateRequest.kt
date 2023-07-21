package com.example.task_app_droid_mediumcom.model

data class TaskCreateRequest(
    val description: String,
    val isReminderSet: Boolean,
    val isTaskOpen: Boolean,
    val priority: Priority
)