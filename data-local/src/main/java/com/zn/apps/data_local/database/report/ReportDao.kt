package com.zn.apps.data_local.database.report

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {

    @Query("SELECT * FROM report ORDER BY datetime(completed_time)")
    fun getPopulatedReports(): Flow<List<PopulatedReportEntity>>

    @Query(
        "SELECT * " +
        "FROM report " +
        "WHERE completed_time BETWEEN :from AND :to"
    )
    fun getPopulatedReports(from: String, to: String): Flow<List<PopulatedReportEntity>>

    @Insert
    suspend fun insert(report: ReportEntity)
}