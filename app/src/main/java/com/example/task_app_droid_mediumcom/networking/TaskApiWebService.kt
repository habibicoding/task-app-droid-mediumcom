package com.example.task_app_droid_mediumcom.networking

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object TaskApiWebService {

    private const val TASK_API_BASE_URL = "https://backend4frontend.onrender.com/"

    private fun createOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .connectTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    fun getTaskApiClient(): TaskApi {
        val gson = GsonBuilder().setLenient().create()
        val okHttpClient = createOkHttpClient()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(TASK_API_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create()) //important
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(TaskApi::class.java)
    }
}