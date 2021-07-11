package com.example.pomodoroapp.manage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.pomodoroapp.R
import com.example.pomodoroapp.data.TaskDatabase
import com.example.pomodoroapp.databinding.FragmentManageTaskBinding
import com.google.android.material.snackbar.Snackbar

/**
 * [Fragment] where the User can edit the values for the task before submitting it.
 * */
class ManageTaskFragment : Fragment() {

    private lateinit var binding: FragmentManageTaskBinding

    private lateinit var viewModelFactory: ManageTaskViewModelFactory

    private var key: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the task id if one was passed in
        arguments.let {
            if (it != null) {
                key = it.getLong("id")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Inflate the layout XML fil and return a binding object instance
//        binding = FragmentManageTaskBinding.inflate(inflater, container, false)
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_manage_task, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create an instance of the ViewModelFactory
        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDao

        viewModelFactory = ManageTaskViewModelFactory(key, dataSource)
        val viewModel = ViewModelProvider(this, viewModelFactory)
                .get(ManageTaskViewModel::class.java)

        // Set the viewModel for data binding - this allows the bound layout access to all
        // the data in the ViewModel
        binding.manageTaskViewModel = viewModel

        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        // Change the task values if the text has been edited in the EditTextField
        binding.taskNameEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                viewModel.setName(viewModel.name.value.toString())
                binding.taskNameEditText.setSelection(binding.taskNameEditText.selectionEnd)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val position = binding.taskNameEditText.selectionEnd
                viewModel.setName(s.toString())
                binding.taskNameEditText.setSelection(position)
            }

            override fun afterTextChanged(s: Editable?) {
//                val position = binding.taskNameEditText.selectionEnd
                viewModel.setName(s.toString())
                binding.taskNameEditText.setSelection(s.toString().length)
            }
        })

        binding.sessionLengthEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                viewModel.setSessionLength(viewModel.sessionTime.value.toString())
                binding.sessionLengthEditText.setSelection(
                    binding.sessionLengthEditText.selectionEnd)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val position = binding.sessionLengthEditText.selectionEnd

                if(s != null)
                    viewModel.setSessionLength(s.toString())

                binding.sessionLengthEditText.setSelection(position)
            }

            override fun afterTextChanged(s: Editable?) {
                val position = binding.sessionLengthEditText.selectionEnd
                if(s == null){
                    // TODO: Show an error that there is no number
                    binding.sessionLengthEditText.error = "Enter a number between 1 to 60"
                }else {
                    viewModel.setSessionLength(s.toString())
                }
                binding.sessionLengthEditText.setSelection(position)
            }
        })

        binding.shortBreakEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                viewModel.setShortBreakLength(viewModel.shortTime.value.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s != null)
                    viewModel.setShortBreakLength(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                if(s == null){
                    // TODO: Show an error that there is no number
                    binding.sessionLengthEditText.error = "Enter a number between 1 to 40"
                }else
                    viewModel.setShortBreakLength(s.toString())
            }
        })

        binding.longBreakEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                viewModel.setLongBreakLength(viewModel.longTime.value.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s != null)
                    viewModel.setLongBreakLength(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                if(s == null){
                    // TODO: Show an error that there is no number
                    binding.sessionLengthEditText.error = "Enter a number between 1 to 50"
                }else
                    viewModel.setLongBreakLength(s.toString())
            }
        })

        binding.numberSessionEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                viewModel.setNumSessions(viewModel.numSessions.value.toString())
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s != null)
                    viewModel.setNumSessions(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                if(s == null){
                    // TODO: Show an error that there is no number
                    binding.sessionLengthEditText.error = "Enter a number between 1 to 10"
                }else
                    viewModel.setNumSessions(s.toString())
            }
        })

        // Show a snackbar that the submission was successful
        viewModel.showSnackbarEvent.observe(viewLifecycleOwner, {
            if(it == true){
                Snackbar.make(
                        requireActivity().findViewById(android.R.id.content),
                        getString(R.string.submit_success), Snackbar.LENGTH_SHORT).show()

                // Reset the state to make sure that the toast is only shown once, even if the
                // device has a configuration change
                viewModel.doneShowingSnackbar()
            }
        })

        // Add an Observer to the state variable for Navigating when the Submit button is pressed
        viewModel.navigateToTask.observe(viewLifecycleOwner, {
            id -> id?.let {
                this.findNavController()
                        .navigate(ManageTaskFragmentDirections
                                .actionManageTaskFragmentToTaskFragment(id))

                viewModel.doneNavigating()
            }
        })
    }

}