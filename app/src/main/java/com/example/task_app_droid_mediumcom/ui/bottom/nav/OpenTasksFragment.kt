package com.example.task_app_droid_mediumcom.ui.bottom.nav

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.task_app_droid_mediumcom.R
import com.example.task_app_droid_mediumcom.model.TaskFetchResponse
import com.example.task_app_droid_mediumcom.model.TaskStatus
import com.example.task_app_droid_mediumcom.ui.view.epoxy.TaskEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class OpenTasksFragment : TaskFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCreateTaskButton()
    }

    override fun onResume() {
        super.onResume()
        callViewModel()
    }

    override fun initializeController() {
        val color = ContextCompat.getColor(requireActivity(), R.color.darker_gray)
        controller = TaskEpoxyController(color)
    }

    override fun callViewModel() {
        viewModel.fetchTasks(TaskStatus.OPEN.toString())
    }

    override fun navigateToEditTask(task: TaskFetchResponse) {
        val action =
            OpenTasksFragmentDirections.actionOpenTaskToEditTask(task)
        findNavController().navigate(action)
    }

    override fun navigateToTaskDetail(task: TaskFetchResponse) {
        val action =
            OpenTasksFragmentDirections.actionOpenTaskToTaskDetail(task.id)
        findNavController().navigate(action)
    }

    private fun setUpCreateTaskButton() {
        binding.fabBtn.setOnClickListener {
            val action = OpenTasksFragmentDirections.actionOpenTaskToCreateTask()
            findNavController().navigate(action)
        }
    }
}