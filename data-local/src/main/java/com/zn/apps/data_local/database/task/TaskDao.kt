package com.zn.apps.data_local.database.task

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Transaction
    @Query("SELECT * FROM task WHERE completed = :completed")
    fun getTasks(completed: Boolean): Flow<List<PopulatedTaskEntity>>

    @Transaction
    @Query("SELECT * FROM task WHERE id = :taskId")
    fun getTask(taskId: String): Flow<PopulatedTaskEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    suspend fun upsertTask(task: TaskEntity) {
        try {
            insertTask(task)
        } catch (e: SQLiteConstraintException) {
            updateTask(task)
        }
    }

    @Delete
    suspend fun deleteTask(task: TaskEntity)

}