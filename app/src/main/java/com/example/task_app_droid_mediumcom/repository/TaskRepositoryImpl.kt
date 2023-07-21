package com.example.task_app_droid_mediumcom.repository

import com.example.task_app_droid_mediumcom.core.BaseRepository
import com.example.task_app_droid_mediumcom.core.ViewState
import com.example.task_app_droid_mediumcom.model.TaskCreateRequest
import com.example.task_app_droid_mediumcom.model.TaskStatus
import com.example.task_app_droid_mediumcom.model.TaskUpdateRequest
import com.example.task_app_droid_mediumcom.networking.TaskApi
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskApiService: TaskApi
) : BaseRepository(), TaskRepository {

    companion object {
        const val HTTP_EXCEPTION = "HttpException"
        const val SUCCESS_NO_CONTENT: Int = 204
    }

    override suspend fun getTasks(status: String?) = performSafeApiCall {
        val taskStatus = TaskStatus.values().find { it.name == status }?.toString()
        taskApiService.getTasks(taskStatus)
    }

    override suspend fun getTaskById(id: String) = performSafeApiCall {
        taskApiService.getTaskById(id)
    }

    override suspend fun createTask(createRequest: TaskCreateRequest) = performSafeApiCall {
        taskApiService.createTask(createRequest)
    }

    override suspend fun canDeleteTask(id: String) = performSafeApiCall {
        val response = taskApiService.canDeleteTask(id)
        if (response.code() != SUCCESS_NO_CONTENT) throw HttpException(response)
        response
    }

    override suspend fun updateTask(id: String, updateRequest: TaskUpdateRequest) = performSafeApiCall {
        taskApiService.updateTaskWithId(id, updateRequest)
    }

    private suspend fun <T : Any> performSafeApiCall(apiCall: suspend () -> T): ViewState<T> {
        return try {
            handleSuccess(apiCall.invoke())
        } catch (error: HttpException) {
            Timber.e("$HTTP_EXCEPTION: ${error.message}")
            handleException(error.code())
        } catch (error: Exception) {
            Timber.e("Unknown exception: ${error.message}")
            ViewState.Error(error)
        }
    }
}
