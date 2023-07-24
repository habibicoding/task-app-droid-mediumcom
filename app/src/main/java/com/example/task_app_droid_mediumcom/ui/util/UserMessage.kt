package com.example.task_app_droid_mediumcom.ui.util

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.example.task_app_droid_mediumcom.R
import com.example.task_app_droid_mediumcom.viewmodel.TaskViewModel

fun displayAlertDialog(id: String, context: Context, message: String, viewModel: TaskViewModel) {
    AlertDialog.Builder(context).apply {
        setMessage(message)
        setPositiveButton(
            context.getString(R.string.delete_task)
        ) { _, _ -> viewModel.deleteTask(id) }
        setNegativeButton(
            context.getString(R.string.cancel_delete_task)
        ) { _, _ ->
            showToastMessage(context, context.getString(R.string.pressed_cancel_delete))
        }
        create()
    }.show()
}

fun showToastMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}