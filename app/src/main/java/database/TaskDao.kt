package database

import alaiz.hashim.taskstodo.Task
import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM task ")
    fun getTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE id=(:id)")
    fun getTask(id: UUID): LiveData<Task?>

    @Query("SELECT * FROM task WHERE flag=(:flag) order by expireDate")
    fun getTasksByFlag(flag: Int):  LiveData<List<Task>>

    @Insert
    fun addTask(task:Task)
    @Update
    fun updateTask(task:Task)
    @Delete
    fun removeTask(task: Task)

}