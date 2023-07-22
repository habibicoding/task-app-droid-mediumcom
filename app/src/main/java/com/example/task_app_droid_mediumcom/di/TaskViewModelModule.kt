package com.example.task_app_droid_mediumcom.di

import com.example.task_app_droid_mediumcom.repository.TaskRepository
import com.example.task_app_droid_mediumcom.repository.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class TaskViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindRepository(repo: TaskRepositoryImpl): TaskRepository
}