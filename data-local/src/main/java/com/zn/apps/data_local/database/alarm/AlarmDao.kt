package com.zn.apps.data_local.database.alarm

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
interface AlarmDao {

    @Transaction
    @Query("SELECT * FROM alarm")
    fun getAlarms(): Flow<List<PopulatedAlarmItemEntity>>

    @Transaction
    @Query("SELECT * FROM alarm WHERE task_id = :taskId")
    fun getAlarm(taskId: String): Flow<PopulatedAlarmItemEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlarm(alarm: AlarmItemEntity)

    @Update
    suspend fun updateAlarm(alarm: AlarmItemEntity)

    @Delete
    suspend fun deleteAlarm(alarm: AlarmItemEntity)

    suspend fun upsertAlarm(alarm: AlarmItemEntity) {
        try {
            insertAlarm(alarm)
        } catch (e: SQLiteConstraintException) {
            updateAlarm(alarm)
        }
    }
}