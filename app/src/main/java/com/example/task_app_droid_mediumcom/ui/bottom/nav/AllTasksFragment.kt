package com.example.task_app_droid_mediumcom.ui.bottom.nav

import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.task_app_droid_mediumcom.R
import com.example.task_app_droid_mediumcom.model.TaskFetchResponse
import com.example.task_app_droid_mediumcom.ui.view.epoxy.TaskEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AllTasksFragment : TaskFragment() {

    override fun onResume() {
        super.onResume()
        callViewModel()
    }

    override fun initializeController() {
        val color = ContextCompat.getColor(requireActivity(), R.color.black)
        controller = TaskEpoxyController(color)
        binding.fabLayout.visibility = View.GONE
    }

    override fun callViewModel() {
        viewModel.fetchTasks(null)
    }

    override fun navigateToEditTask(task: TaskFetchResponse) {
        val action =
            AllTasksFragmentDirections.actionAllTasksToEditTask(task)
        findNavController().navigate(action)
    }

    override fun navigateToTaskDetail(task: TaskFetchResponse) {
        val action =
            AllTasksFragmentDirections.actionAllTasksToTaskDetail(task.id)
        findNavController().navigate(action)
    }
}