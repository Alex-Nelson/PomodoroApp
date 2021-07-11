package com.example.pomodoroapp.list

import android.widget.Button
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.example.pomodoroapp.R
import com.example.pomodoroapp.data.Task

/**
 * The Bindings for the TaskListFragment. It binds the task's name and an OnClickListener to each
 * button (if there are any).
 * */

@BindingAdapter("setTaskName")
fun Button.setName(item: Task) {
    text = context.resources.getString(R.string.task_name_string, item.taskName)
}

@BindingAdapter("buttonListener")
fun Button.onClick(item: Task) {
    setOnClickListener {
        // Create an action from TaskList to TaskFragment using the required arguments
        val action = TaskListFragmentDirections
                .actionTaskListFragmentToTaskFragment(item.taskId)

        // Navigate using that action
        this.findNavController().navigate(action)
    }
}