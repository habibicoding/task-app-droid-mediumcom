package com.example.task_app_droid_mediumcom.ui.bottom.nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.task_app_droid_mediumcom.R
import com.example.task_app_droid_mediumcom.core.ViewBindingFragment
import com.example.task_app_droid_mediumcom.core.ViewState
import com.example.task_app_droid_mediumcom.databinding.FragmentTaskBinding
import com.example.task_app_droid_mediumcom.model.TaskFetchResponse
import com.example.task_app_droid_mediumcom.ui.util.displayAlertDialog
import com.example.task_app_droid_mediumcom.ui.util.showToastMessage
import com.example.task_app_droid_mediumcom.ui.view.SwipeGestures
import com.example.task_app_droid_mediumcom.ui.view.epoxy.TaskEpoxyController
import com.example.task_app_droid_mediumcom.viewmodel.TaskViewModelImpl
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
abstract class TaskFragment : ViewBindingFragment<FragmentTaskBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    protected val viewModel by viewModels<TaskViewModelImpl>()
    protected lateinit var controller: TaskEpoxyController
    private val tasks = mutableListOf<TaskFetchResponse>()

    protected abstract fun initializeController()
    protected abstract fun callViewModel()
    protected abstract fun navigateToEditTask(task: TaskFetchResponse)
    protected abstract fun navigateToTaskDetail(task: TaskFetchResponse)

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTaskBinding = FragmentTaskBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeGestures()
        initializeController()
        setUpRecyclerView()
        observeTaskLiveData()
        observeDeleteTaskLiveData()
        clickOnRetry()
        setUpSwipeRefresh()
    }

    private fun setupSwipeGestures() {
        val swipeGestures = object : SwipeGestures(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val myTask = controller.getTaskById(viewHolder.absoluteAdapterPosition)
                myTask.let { task ->
                    when (direction) {
                        ItemTouchHelper.LEFT -> {
                            displayAlertDialog(
                                task.id.toString(),
                                requireContext(),
                                getString(R.string.delete_task_headline),
                                viewModel
                            )
                            controller.notifyModelChanged(viewHolder.absoluteAdapterPosition)
                        }

                        ItemTouchHelper.RIGHT -> navigateToEditTask(task)
                    }
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipeGestures)
        touchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onRefresh() {
        callViewModel()
    }

    private fun setUpSwipeRefresh() {
        binding.swipeRefresh.let {
            it.setOnRefreshListener(this)
            it.setColorSchemeResources(R.color.purple_200)
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            this.setHasFixedSize(true)
            this.itemAnimator = DefaultItemAnimator()
            this.adapter = controller.adapter
        }
    }

    private fun observeTaskLiveData() {
        viewModel.tasks.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ViewState.Loading -> {
                    binding.recyclerView.visibility = View.GONE
                    binding.shimmerFrame.startShimmerAnimation()
                }

                is ViewState.Success -> {
                    this.tasks.clear()
                    if (response.data.isEmpty()) {
                        showEmptyScreen()
                    } else {
                        showArticlesOnScreen()
                    }
                    val fetchedTasks = response.data.map { task ->
                        task.onClick = View.OnClickListener { navigateToTaskDetail(task) }
                        task
                    }
                    this.tasks.addAll(fetchedTasks)
                    this.controller.setTasks(this.tasks)

                    if (controller.getNumberOfMyTasks() == 0) {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.no_tasks_found),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }

                is ViewState.Error -> {
                    showEmptyScreen()
                }
            }
        }
    }

    private fun showEmptyScreen() {
        with(binding) {
            shimmerFrame.stopShimmerAnimation()
            shimmerFrame.visibility = View.GONE
            recyclerView.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
            retryFetchButton.visibility = View.VISIBLE
        }
        controller.setTasks(emptyList())
    }

    private fun showArticlesOnScreen() {
        with(binding) {
            shimmerFrame.stopShimmerAnimation()
            shimmerFrame.visibility = View.GONE
            emptyText.visibility = View.GONE
            retryFetchButton.visibility = View.GONE
            swipeRefresh.isRefreshing = false
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun observeDeleteTaskLiveData() {
        viewModel.isDeleteSuccessful.observe(viewLifecycleOwner) { isSuccessful ->
            if (isSuccessful) {
                showToastMessage(requireContext(), getString(R.string.task_request_success_message))
                callViewModel()
            } else {
                showToastMessage(requireContext(), getString(R.string.task_request_failure_message))
                Timber.d("Delete status: $isSuccessful")
            }
        }
    }

    private fun clickOnRetry() {
        with(binding) {
            retryFetchButton.setOnClickListener { button ->
                button.visibility = View.GONE
                emptyText.visibility = View.GONE
                shimmerFrame.startShimmerAnimation()
                shimmerFrame.visibility = View.VISIBLE

                callViewModel()
            }
        }
    }
}
