package com.example.task_app_droid_mediumcom.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class ViewBindingFragment<VB : ViewBinding> : Fragment() {

    abstract fun createBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    protected lateinit var binding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = createBinding(inflater, container).also { binding = it }.root
}
