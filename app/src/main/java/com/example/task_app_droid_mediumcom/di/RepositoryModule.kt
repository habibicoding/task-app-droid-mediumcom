package com.example.task_app_droid_mediumcom.di

import com.example.task_app_droid_mediumcom.networking.TaskApi
import com.example.task_app_droid_mediumcom.networking.TaskApiWebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideTaskApiWebService(): TaskApi = TaskApiWebService.getTaskApiClient()
}