package com.example.pomodoroapp.task

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pomodoroapp.R
import com.example.pomodoroapp.data.TaskDatabase
import com.example.pomodoroapp.databinding.FragmentTaskBinding
import com.example.pomodoroapp.util.Notifications
import com.example.pomodoroapp.util.TimerNotifications
import com.example.pomodoroapp.util.TimerUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

/**
 * Fragment where the Pomodoro timer is used
 * */
class TaskFragment : Fragment() {

    //Binding object instance with access to the views in the fragment_task.xml layout
    private lateinit var binding: FragmentTaskBinding

    private var id: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the taskId from the Fragment arguments
        arguments?.let {
            id = it.getLong("id")
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Inflate the layout XML file and return a binding object instance
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_task, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDao

        val viewModelFactory = TaskViewModelFactory(id, dataSource)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TaskViewModel::class.java)

        // Set the viewModel for data binding - this allows the bound layout access
        // to all the data in the ViewModel
        binding.taskViewModel = viewModel

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Add an observer for when the user is navigating back to the ManageTaskFragment
        viewModel.navigateToManageTask.observe(viewLifecycleOwner, { taskId ->
            taskId?.let {
                this.findNavController()
                        .navigate(TaskFragmentDirections
                                .actionTaskFragmentToManageTaskFragment(taskId))

                viewModel.doneNavigatingToManageTask()
            }
        })

        // Show a snackbar that the task was successfully deleted
        viewModel.showSnackbarDelete.observe(viewLifecycleOwner, {
            if(it == true){
                Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.delete_success), Snackbar.LENGTH_SHORT).show()

                // Reset the state to make sure that the toast is only shown once, even if the device
                // has a configuration change
                viewModel.doneShowingSnackbarDelete()
            }

        })

        // Add an observer for when the user deletes a task
        viewModel.navigateToTaskList.observe(viewLifecycleOwner, {
            if(it == true){
                this.findNavController()
                        .navigate(TaskFragmentDirections.actionTaskFragmentToTaskListFragment())

                // Reset the state to make sure that the toast is only shown once, even if the device
                // has a configuration change
                viewModel.doneNavigatingToTaskList()
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_task_option -> {
                // Navigate to the ManageTaskFragment to edit it
                this.findNavController().navigate(
                        TaskFragmentDirections.actionTaskFragmentToManageTaskFragment(id))
                binding.taskViewModel?.doneNavigatingToManageTask()
                true
            }
            R.id.delete_task_option -> {
                // Allow user to select if they want delete the current task
                showDeleteDialog()
                true
            }
            // Otherwise, do nothing and use the core event handling
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        // Remove the alarm
        TimerUtil.removeAlarm(requireContext())

        // Restore the timer in the fragment using the ViewModel

        // Hide notification
        TimerNotifications.hideTimerNotification(requireContext())
    }

    /**
     * Destroy all notifications when the fragment's view is destroyed
     * */
    override fun onDestroyView() {
        super.onDestroyView()

        Notifications.hideNotifications(requireContext())
    }

    /**
     * When the fragment loses the user focus, set up the timer notification so it continues in the
     * background
     * */
    override fun onPause() {
        super.onPause()

        // Set up the notifications
        binding.taskViewModel?.setUpNotifications(requireContext())
    }

    /**
     * Creates and shows an AlertDialog for deleting a task.
     * */
    private fun showDeleteDialog(){

        // Pause the timer when the dialog pops up
        binding.taskViewModel?.onPause()

        MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.delete_task))
                .setCancelable(true)
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.delete)){ _, _ ->
                    binding.taskViewModel?.deleteTask()
                }.show()
    }

}