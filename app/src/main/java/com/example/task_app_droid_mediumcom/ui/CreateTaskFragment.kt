package com.example.task_app_droid_mediumcom.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.task_app_droid_mediumcom.R
import com.example.task_app_droid_mediumcom.core.ViewBindingFragment
import com.example.task_app_droid_mediumcom.core.ViewState
import com.example.task_app_droid_mediumcom.databinding.FragmentCreateTaskBinding
import com.example.task_app_droid_mediumcom.model.Priority
import com.example.task_app_droid_mediumcom.model.TaskCreateRequest
import com.example.task_app_droid_mediumcom.ui.util.showToastMessage
import com.example.task_app_droid_mediumcom.viewmodel.TaskViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CreateTaskFragment : ViewBindingFragment<FragmentCreateTaskBinding>() {

    companion object {
        private const val REQUEST_SUCCESS = "Request was successful!"
        private const val REQUEST_FAILURE = "Request couldn't be processed!"
    }

    private val viewModel by viewModels<TaskViewModelImpl>()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCreateTaskBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeCreateTaskLiveData()

        binding.createTaskBtn.setOnClickListener {
            val task = createTaskItem()
            task?.let { viewModel.createTask(it) }
        }
    }

    private fun createTaskItem(): TaskCreateRequest? {
        val description = binding.createTaskDescriptionInput.text.toString()
        val isSetReminderSet = binding.createTaskSetReminderCheckBox.isChecked

        if (description.isEmpty()) {
            showToastMessage(requireContext(), getString(R.string.enter_description))
            return null
        }

        val priority = when {
            binding.priorityLow.isChecked -> Priority.LOW
            binding.priorityMedium.isChecked -> Priority.MEDIUM
            else -> Priority.HIGH
        }

        return TaskCreateRequest(
            description = description,
            priority = priority,
            isReminderSet = isSetReminderSet,
            isTaskOpen = true
        )
    }

    private fun observeCreateTaskLiveData() {
        viewModel.task.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ViewState.Loading -> {
                    Timber.d("data is loading ${response.extractData}")
                }

                is ViewState.Success -> {
                    showToastMessage(requireContext(), REQUEST_SUCCESS)
                    findNavController().popBackStack()
                }

                is ViewState.Error -> {
                    val errorMessage = response.exception.message ?: REQUEST_FAILURE
                    showToastMessage(requireContext(), errorMessage)
                }
            }
        }
    }
}
