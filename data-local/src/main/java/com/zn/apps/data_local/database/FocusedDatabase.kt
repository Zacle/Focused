package com.zn.apps.data_local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zn.apps.data_local.database.alarm.AlarmDao
import com.zn.apps.data_local.database.alarm.AlarmItemEntity
import com.zn.apps.data_local.database.project.ProjectDao
import com.zn.apps.data_local.database.project.ProjectEntity
import com.zn.apps.data_local.database.report.ReportDao
import com.zn.apps.data_local.database.report.ReportEntity
import com.zn.apps.data_local.database.tag.TagDao
import com.zn.apps.data_local.database.tag.TagEntity
import com.zn.apps.data_local.database.task.TaskDao
import com.zn.apps.data_local.database.task.TaskEntity
import com.zn.apps.data_local.database.util.DateConverter

@Database(
    entities = [
        TaskEntity::class,
        ProjectEntity::class,
        TagEntity::class,
        ReportEntity::class,
        AlarmItemEntity::class
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ]
)
@TypeConverters(DateConverter::class)
abstract class FocusedDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

    abstract fun tagDao(): TagDao

    abstract fun projectDao(): ProjectDao

    abstract fun reportDao(): ReportDao

    abstract fun alarmDao(): AlarmDao
}