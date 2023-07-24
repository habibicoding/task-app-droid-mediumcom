package com.example.task_app_droid_mediumcom

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TaskApp : Application() {

    companion object {
        lateinit var instance: TaskApp
    }

    init {
        instance = this
    }
}