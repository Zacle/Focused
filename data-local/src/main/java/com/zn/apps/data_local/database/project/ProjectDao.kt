package com.zn.apps.data_local.database.project

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
interface ProjectDao {

    @Transaction
    @Query("SELECT * FROM project")
    fun getProjects(): Flow<List<PopulatedProjectEntity>>

    @Query("SELECT * FROM project WHERE id = :projectId")
    fun getProject(projectId: String): Flow<ProjectEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    suspend fun upsertProject(project: ProjectEntity) {
        try {
            insertProject(project)
        } catch (e: SQLiteConstraintException) {
            updateProject(project)
        }
    }

    @Delete
    suspend fun deleteProject(project: ProjectEntity)
}