package com.example.task_app_droid_mediumcom.repository

import com.example.task_app_droid_mediumcom.BaseRepositoryTest
import com.example.task_app_droid_mediumcom.FileReader
import com.example.task_app_droid_mediumcom.core.BaseRepository.Companion.SOMETHING_WRONG
import com.example.task_app_droid_mediumcom.core.ViewState
import com.example.task_app_droid_mediumcom.model.Priority
import com.example.task_app_droid_mediumcom.model.TaskCreateRequest
import com.example.task_app_droid_mediumcom.model.TaskFetchResponse
import com.example.task_app_droid_mediumcom.model.TaskUpdateRequest
import com.example.task_app_droid_mediumcom.networking.TaskApi
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


internal class TaskRepositoryImplTest : BaseRepositoryTest() {

    companion object {
        private const val TASKS_RESPONSE = "get_tasks_response.json"
        private const val TASK_BY_ID_RESPONSE = "get_task_by_id_response.json"
        private const val TASK_POST_REQUEST = "post_request_task.json"
        private const val TASK_PATCH_REQUEST = "patch_request_task.json"
        private const val INTERNAL_SERVER_ERROR: Int = 500
    }

    private val gson = GsonBuilder().create()

    private val exception = Exception("triggered exception")

    private val createRequest = TaskCreateRequest(
        description = "test data",
        isReminderSet = true,
        isTaskOpen = true,
        priority = Priority.LOW
    )

    private val updateRequest = TaskUpdateRequest(
        description = "Try new Shawarma place",
        isReminderSet = null,
        isTaskOpen = null,
        priority = null
    )

    private lateinit var taskApi: TaskApi
    private lateinit var objectUnderTest: TaskRepository

    private val mockTaskApi = mockk<TaskApi>()
    private val mockTaskRepository = TaskRepositoryImpl(mockTaskApi)

    @Before
    override fun setUp() {
        taskApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(ScalarsConverterFactory.create()) //important
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(TaskApi::class.java)

        objectUnderTest = TaskRepositoryImpl(taskApi)
    }

    @Test
    fun `when fetching all tasks then check for success response`() {
        // given
        val jsonArray = getJsonString<List<TaskFetchResponse>>(TASKS_RESPONSE)
        val listType = object : TypeToken<ArrayList<TaskFetchResponse?>?>() {}.type
        val expectedItems = gson.fromJson<List<TaskFetchResponse>>(jsonArray, listType)

        mockWebServer.enqueue(MockResponse().setBody(FileReader(TASKS_RESPONSE).content))

        runBlocking {
            // when
            val actualResult = objectUnderTest.getTasks(null).extractData
            // then
            assertEquals(expectedItems, actualResult)
        }
    }

    @Test
    fun `when fetching all tasks then then check for http exception`() {
        // given
        mockWebServer.enqueue(MockResponse().setResponseCode(INTERNAL_SERVER_ERROR))

        runBlocking {
            // when
            val actualResult = objectUnderTest.getTasks(null)

            // then
            assertTrue(actualResult is ViewState.Error)
            assertEquals(SOMETHING_WRONG, (actualResult as ViewState.Error).exception.message)
        }
    }

    @Test
    fun `when fetching all tasks then then check for unknown exception`() {
        // given
        coEvery { mockTaskRepository.getTasks(null) } throws exception

        runBlocking {
            // when
            val actualResult = mockTaskRepository.getTasks(null)

            // then
            assertTrue(actualResult is ViewState.Error)
            assertEquals(exception.message, (actualResult as ViewState.Error).exception.message)
        }
    }

    @Test
    fun `when patch task request sent then check for success response`() {
        // given
        val expectedTask = getDataClass<TaskFetchResponse>(TASK_PATCH_REQUEST)
        mockWebServer.enqueue(MockResponse().setBody(FileReader(TASK_PATCH_REQUEST).content))

        runBlocking {
            // when
            val actualTask = objectUnderTest.updateTask("2", updateRequest).extractData
            // then
            assertEquals(expectedTask, actualTask)
        }
    }

    @Test
    fun `when patch task request sent then check for http exception`() {
        // given
        mockWebServer.enqueue(MockResponse().setResponseCode(INTERNAL_SERVER_ERROR))

        runBlocking {
            // when
            val actualResult = objectUnderTest.updateTask("19", updateRequest)

            // then
            assertTrue(actualResult is ViewState.Error)
            assertEquals(SOMETHING_WRONG, (actualResult as ViewState.Error).exception.message)
        }
    }

    @Test
    fun `when patch task request sent then check for unknown exception`() {
        // given
        coEvery { mockTaskRepository.updateTask(any(), any()) } throws exception

        runBlocking {
            // when
            val actualResult = mockTaskRepository.updateTask("111", updateRequest)

            // then
            assertTrue(actualResult is ViewState.Error)
            assertEquals(exception.message, (actualResult as ViewState.Error).exception.message)
        }
    }
}


