package com.example.task_app_droid_mediumcom.repository

import com.example.task_app_droid_mediumcom.core.ViewState
import com.example.task_app_droid_mediumcom.model.TaskCreateRequest
import com.example.task_app_droid_mediumcom.model.TaskFetchResponse
import com.example.task_app_droid_mediumcom.model.TaskUpdateRequest
import retrofit2.Response

interface TaskRepository {

    suspend fun getTasks(status: String?): ViewState<List<TaskFetchResponse>>

    suspend fun getTaskById(id: String): ViewState<TaskFetchResponse>

    suspend fun createTask(createRequest: TaskCreateRequest): ViewState<TaskFetchResponse>

    suspend fun canDeleteTask(id: String): ViewState<Response<Boolean>>

    suspend fun updateTask(
        id: String,
        updateRequest: TaskUpdateRequest
    ): ViewState<TaskFetchResponse>
}