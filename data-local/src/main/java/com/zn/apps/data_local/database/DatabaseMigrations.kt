package com.zn.apps.data_local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `report` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `completed_time` TEXT NOT NULL, `elapsed_time` INTEGER NOT NULL, `task_id` TEXT, `tag_id` TEXT, `project_id` TEXT, FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE SET NULL, FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE SET NULL, FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE SET NULL)")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_report_task_id` ON `report` (`task_id`)")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_report_tag_id` ON `report` (`tag_id`)")
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_report_project_id` ON `report` (`project_id`)")
        }
    }
}