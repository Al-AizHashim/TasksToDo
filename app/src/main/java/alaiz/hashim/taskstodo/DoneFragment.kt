package alaiz.hashim.taskstodo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat



class DoneFragment : Fragment() {

    @SuppressLint("SimpleDateFormat")
    var formatter: SimpleDateFormat = SimpleDateFormat("EEEE, MMM d, yyyy")
    private lateinit var taskRecyclerView: RecyclerView
    private  var finish_date: Long = 0
    private  var   current_time =System.currentTimeMillis()
    private var adapter: TaskAdapter? = null
    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailViewModel::class.java)
    }

    private val taskViewModel: TaskViewModel by lazy {
        ViewModelProvider(this).get(TaskViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_done, container, false)


        taskRecyclerView =
            view.findViewById(R.id.task_Done_recycler_view) as RecyclerView
        taskRecyclerView.layoutManager = LinearLayoutManager(context)
        taskRecyclerView.adapter = adapter

        return view
    }


    private fun updateUI(tasks: List<Task>) {
        adapter?.let {
            it.tasks = tasks
        } ?: run {
            adapter = TaskAdapter(tasks)
        }
        taskRecyclerView.adapter = adapter

    }

    private inner class TaskHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var task: Task
        private val nextBtn: Button = itemView.findViewById(R.id.nextDone)
        private val previousDoneBtn: Button = itemView.findViewById(R.id.previousDoneBtn)
        private val taskTitleTV: TextView = itemView.findViewById(R.id.taskDoneTitleTV)
        private val taskDatetextView: TextView = itemView.findViewById(R.id.taskDatedonetextView)
        var taskDoneItemList: ConstraintLayout =itemView.findViewById(R.id.taskDoneItemList)


        private val deleteTaskBtn: Button = itemView.findViewById(R.id.deleteTaskDoneBtn)

        init {
            itemView.setOnClickListener(this)

        }

        @SuppressLint("ResourceAsColor")
        fun bind(task: Task) {

            deleteTaskBtn.setOnClickListener {
                taskDetailViewModel.deleteTask(task)
            }
            previousDoneBtn.setOnClickListener {
                task.flag=1
                taskDetailViewModel.saveTask(task)
            }
            nextBtn.isEnabled=false


            this.task = task
            taskTitleTV.text = this.task.title
            taskDatetextView.text = formatter.format(this.task.expireDate)
            // finish_date= this.task.expireDate.time
            // var leftDate=finish_date - current_time
            //var criticalTime:Long=259200000
            //if (leftDate<=criticalTime)
//            taskListItem.setBackgroundColor(Color.parseColor("##ffff99"))
            // taskListItem.setBackgroundColor(Color.RED)
            taskDoneItemList.setBackgroundColor(Color.parseColor("#b3ffb3"))



        }

        override fun onClick(v: View) {


        }


    }

    private inner class TaskAdapter(var tasks: List<Task>) : RecyclerView.Adapter<TaskHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : TaskHolder {

            val view = layoutInflater.inflate(R.layout.task_done_item_list, parent, false)

            return TaskHolder(view)
        }

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val task = tasks[position]
            holder.bind(task)
        }

        override fun getItemCount(): Int {
            return tasks.size
        }
    }

    companion object {
        fun newInstance() = DoneFragment()


    }

    override fun onStart() {
        super.onStart()
        taskViewModel.taskDoneListLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { tasks ->
                tasks?.let {

                    updateUI(tasks)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()

    }

}

