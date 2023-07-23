package com.example.task_app_droid_mediumcom.ui.view.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.task_app_droid_mediumcom.itemTask
import com.example.task_app_droid_mediumcom.model.TaskFetchResponse

class TaskEpoxyController(private val cardBackGroundColor: Int) : EpoxyController() {

    private val tasks = mutableListOf<TaskFetchResponse>()

    override fun buildModels() {
        tasks.forEachIndexed { _, model ->
            itemTask {
                id(model.id)
                taskResponse(model)
                bgColor(this@TaskEpoxyController.cardBackGroundColor)
            }
        }
    }

    fun setTasks(taskItems: List<TaskFetchResponse>) {
        this.tasks.clear()
        this.tasks.addAll(taskItems)
        requestModelBuild()
    }

    fun getNumberOfMyTasks(): Int = tasks.size

    fun getTaskById(index: Int): TaskFetchResponse = tasks[index]
}