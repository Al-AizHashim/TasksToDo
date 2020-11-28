package alaiz.hashim.taskstodo


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Color.RED
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.hardware.camera2.params.RggbChannelVector.RED
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.xmlpull.v1.XmlPullParser
import java.text.SimpleDateFormat
import java.util.*

//lateinit var taskListItem: ConstraintLayout
class ToDoFragment : Fragment() {

    interface Callbacks {
        fun onTaskSelected(taskId: UUID)
    }

    private var callbacks: Callbacks? = null

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
        callbacks = context as Callbacks?
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
        val view = inflater.inflate(R.layout.fragment_to_do, container, false)


        taskRecyclerView =
            view.findViewById(R.id.task_recycler_view) as RecyclerView
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
        private val moveToInProgress: Button = itemView.findViewById(R.id.moveToInProgress)
        private val moveToToDo: Button = itemView.findViewById(R.id.button)
        private val taskTitleTV: TextView = itemView.findViewById(R.id.taskTitleTV)
        private val taskDatetextView: TextView = itemView.findViewById(R.id.taskDatetextView)
        var taskListItem:ConstraintLayout=itemView.findViewById(R.id.taskItemList)

        private val deleteTaskBtn: Button = itemView.findViewById(R.id.deleteTaskBtn)

        init {
            itemView.setOnClickListener(this)

        }

        @SuppressLint("ResourceAsColor")
        fun bind(task: Task) {

            deleteTaskBtn.setOnClickListener {
                taskDetailViewModel.deleteTask(task)
            }
            moveToInProgress.setOnClickListener {
                task.flag=1
                taskDetailViewModel.saveTask(task)
            }
            moveToToDo.isEnabled=false

            this.task = task
            taskTitleTV.text = this.task.title
            taskDatetextView.text = formatter.format(this.task.expireDate)
            finish_date= this.task.expireDate.time
            var leftDate=finish_date - current_time
            var criticalTime:Long=259200000
            if(leftDate>criticalTime)
                taskListItem.background.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
               // taskListItem.setBackgroundColor(Color.parseColor("#FFFFFF"))
            else if (leftDate<=0L)
                taskListItem.background.setColorFilter(Color.parseColor("#ffb3b3"), PorterDuff.Mode.SRC_ATOP)
               //taskListItem.setBackgroundColor(Color.parseColor("#ffb3b3"))
            else
                taskListItem.background.setColorFilter(Color.parseColor("#ffce99"), PorterDuff.Mode.SRC_ATOP)
                //taskListItem.setBackgroundColor(Color.parseColor("#ffce99"))

        }

        override fun onClick(v: View) {
            callbacks?.onTaskSelected(task.id)
        }


    }

    private inner class TaskAdapter(var tasks: List<Task>) : RecyclerView.Adapter<TaskHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : TaskHolder {

            val view = layoutInflater.inflate(R.layout.task_item_list, parent, false)

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
        fun newInstance() = ToDoFragment()


    }

    override fun onStart() {
        super.onStart()
            taskViewModel.taskTodoListLiveData.observe(
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
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_task_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_task -> {
                val task = Task()
                taskViewModel.addTask(task)
                callbacks?.onTaskSelected(task.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }


}



