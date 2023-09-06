package com.zn.apps.data_local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zn.apps.data_local.database.project.ProjectDao
import com.zn.apps.data_local.database.project.ProjectEntity
import com.zn.apps.data_local.database.tag.TagDao
import com.zn.apps.data_local.database.tag.TagEntity
import com.zn.apps.data_local.database.task.TaskDao
import com.zn.apps.data_local.database.task.TaskEntity
import com.zn.apps.data_local.database.util.DateConverter

@Database(
    entities = [
        TaskEntity::class,
        ProjectEntity::class,
        TagEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class)
abstract class FocusedDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

    abstract fun tagDao(): TagDao

    abstract fun projectDao(): ProjectDao
}