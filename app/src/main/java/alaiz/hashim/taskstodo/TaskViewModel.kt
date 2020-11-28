package alaiz.hashim.taskstodo

import androidx.lifecycle.ViewModel
import java.util.*

class TaskViewModel:ViewModel() {

    private val taskRepository = TaskRepository.get()
    val taskListLiveData = taskRepository.getTasks()
    val taskTodoListLiveData=taskRepository.getTasksByFlag(0)
    val taskInProgressListLiveData=taskRepository.getTasksByFlag(1)
    val taskDoneListLiveData=taskRepository.getTasksByFlag(2)

    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }

}