package com.example.task_app_droid_mediumcom

import android.app.Application
import com.airbnb.epoxy.databinding.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TaskApp : Application() {

    companion object {
        lateinit var instance: TaskApp
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}