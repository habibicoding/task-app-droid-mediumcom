package com.example.task_app_droid_mediumcom.model

import android.os.Parcelable
import android.view.View
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskFetchResponse(
    val id: Long,
    val description: String,
    val isReminderSet: Boolean?,
    val isTaskOpen: Boolean?,
    val createdOn: String?,
    val priority: Priority?
) : Parcelable {

    @IgnoredOnParcel
    var onClick: View.OnClickListener? = null
}