package com.example.task_app_droid_mediumcom.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.task_app_droid_mediumcom.core.ViewBindingFragment
import com.example.task_app_droid_mediumcom.databinding.FragmentTaskDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TaskDetailFragment : ViewBindingFragment<FragmentTaskDetailBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTaskDetailBinding.inflate(inflater)
}