package alaiz.hashim.taskstodo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class TaskDetailViewModel:ViewModel (){
    private val taskRepository = TaskRepository.get()
    private val TaskflagLiveData = MutableLiveData<Int>()
    private val TaskIdLiveData = MutableLiveData<UUID>()
    var taskLiveData: LiveData<Task?> =
    Transformations.switchMap(TaskIdLiveData) { taskId ->
        taskRepository.getTask(taskId)
    }
    var taskinprogressLiveData: LiveData<List<Task>> =
        Transformations.switchMap(TaskflagLiveData) { flag ->
            taskRepository.getTasksByFlag(flag)
        }

    fun loadTask(TaskId: UUID) {
        TaskIdLiveData.value = TaskId
    }
    fun saveTask(task: Task) {
        taskRepository.updateTask(task)
    }
    fun deleteTask(task: Task){
        taskRepository.removeTask(task)
    }
    fun loadInprogressTasks(flag:Int){
        taskRepository.getTasksByFlag(flag)
    }
}