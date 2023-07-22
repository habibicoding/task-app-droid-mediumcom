package com.example.task_app_droid_mediumcom.viewmodel

import com.example.task_app_droid_mediumcom.model.TaskCreateRequest
import com.example.task_app_droid_mediumcom.model.TaskUpdateRequest

interface TaskViewModel {

    fun fetchTasks(status: String?)

    fun fetchTaskById(id: String)

    fun createTask(createRequest: TaskCreateRequest)

    fun deleteTask(id: String)

    fun updateTask(id: String, updateRequest: TaskUpdateRequest)
}