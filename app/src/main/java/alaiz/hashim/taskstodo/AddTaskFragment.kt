package alaiz.hashim.taskstodo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import android.text.Editable
import android.text.TextWatcher
import android.widget.Button

import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.task_item_list.*
import java.util.*

private const val REQUEST_DATE = 0
private const val DIALOG_DATE = "DialogDate"
private const val ARG_TASK_ID = "task_id"

class AddTaskFragment : Fragment(), DatePickerFragment.Callbacks {
    private lateinit var task: Task
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var flag: EditText
    private lateinit var details: EditText
    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()
        val taskId: UUID = arguments?.getSerializable(ARG_TASK_ID) as UUID
        taskDetailViewModel.loadTask(taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)

        titleField = view.findViewById(R.id.task_title) as EditText
        dateButton = view.findViewById(R.id.task_date) as Button
        flag = view.findViewById(R.id.flag)
        details = view.findViewById(R.id.task_details)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val taskId = arguments?.getSerializable(ARG_TASK_ID) as UUID
        taskDetailViewModel.loadTask(taskId)
        taskDetailViewModel.taskLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { task ->
                task?.let {
                    this.task = task
                    updateUI()
                }
            })
        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.supportActionBar?.setTitle("New Task")
    }

    override fun onStart() {
        super.onStart()
        taskDetailViewModel.saveTask(task)

        val detailsWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                task.detail= s.toString()
            }
        }
        details.addTextChangedListener(detailsWatcher)

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                task.title = sequence.toString()


            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        titleField.addTextChangedListener(titleWatcher)





        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(task.expireDate).apply {
                setTargetFragment(this@AddTaskFragment, REQUEST_DATE)
                show(this@AddTaskFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        taskDetailViewModel.saveTask(task)
    }

    private fun updateUI() {
        titleField.setText(task.title)
        dateButton.text = task.expireDate.toString()
        details.setText(task.detail)
        flag.setText(task.flag.toString())

    }

    companion object {
        fun newInstance(taskId: UUID): AddTaskFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TASK_ID, taskId)
            }
            return AddTaskFragment().apply {
                arguments = args
            }
        }
    }

    override fun onDateSelected(expireDate: Date) {
        task.expireDate = expireDate
        updateUI()
    }
}