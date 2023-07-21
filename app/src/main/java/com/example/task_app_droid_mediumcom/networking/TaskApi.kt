package com.example.task_app_droid_mediumcom.networking

import com.example.task_app_droid_mediumcom.model.TaskCreateRequest
import com.example.task_app_droid_mediumcom.model.TaskFetchResponse
import com.example.task_app_droid_mediumcom.model.TaskUpdateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TaskApi {
    companion object {
        private const val TASK_API_TASKS_ENDPOINT = "api/v1/tasks"
        private const val TASK_API_TASK_ID_ENDPOINT = "api/v1/tasks/{id}"
    }

    @GET(TASK_API_TASKS_ENDPOINT)
    suspend fun getTasks(@Query("status") status: String?): List<TaskFetchResponse>

    @GET(TASK_API_TASK_ID_ENDPOINT)
    suspend fun getTaskById(@Path("id") id: String): TaskFetchResponse

    @POST(TASK_API_TASKS_ENDPOINT)
    suspend fun createTask(@Body createRequest: TaskCreateRequest): TaskFetchResponse

    @DELETE(TASK_API_TASK_ID_ENDPOINT)
    suspend fun canDeleteTask(@Path("id") id: String): Response<Boolean>

    @PATCH(TASK_API_TASK_ID_ENDPOINT)
    suspend fun updateTaskWithId(
        @Path("id") id: String,
        @Body updateRequest: TaskUpdateRequest
    ): TaskFetchResponse
}