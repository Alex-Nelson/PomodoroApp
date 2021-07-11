package com.example.pomodoroapp.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pomodoroapp.R
import com.example.pomodoroapp.data.TaskDatabase
import com.example.pomodoroapp.databinding.FragmentTaskListBinding

/**
 * Entry fragment for the app. Displays a [RecylerView] of task names.
 * */
class TaskListFragment : Fragment() {

    private lateinit var binding: FragmentTaskListBinding

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        // Retrieve and inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_task_list, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val application = requireNotNull(this.activity).application
        val dataSource = TaskDatabase.getInstance(application).taskDao

        val viewModelFactory = TaskListViewModelFactory(dataSource)
        val viewModel = ViewModelProvider(this, viewModelFactory)
            .get(TaskListViewModel::class.java)

        // Set the viewModel for data binding
//        recyclerView = view.findViewById(R.id.recycler_view)
//        recyclerView.adapter = viewModel.taskList?.let { TaskAdapter(it) }
        val adapter = TaskAdapter()

        viewModel.taskList.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })
        recyclerView.adapter = adapter

        binding.createTaskFAB.setOnClickListener { viewModel.onFabClicked() }

        // Navigate to the next screen to create a task when teh FAB is clicked
        viewModel.navigateToManageTask.observe(viewLifecycleOwner, {
            if(it == true){
                this.findNavController()
                    .navigate(TaskListFragmentDirections
                        .actionTaskListFragmentToManageTaskFragment(0))

                viewModel.doneNavigatingToManageTask()
            }
        })
    }

}