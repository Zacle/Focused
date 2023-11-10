package com.zn.apps.data_local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zn.apps.model.datastore.DEFAULT_TASK_REMINDER_MINUTES

object DatabaseMigrations {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `report` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `completed_time` TEXT NOT NULL, `elapsed_time` INTEGER NOT NULL, `task_id` TEXT, `tag_id` TEXT, `project_id` TEXT, FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE SET NULL, FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`) ON DELETE SET NULL, FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE SET NULL)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_report_task_id` ON `report` (`task_id`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_report_tag_id` ON `report` (`tag_id`)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_report_project_id` ON `report` (`project_id`)")
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `alarm` (`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `task_id` TEXT NOT NULL, `remind_at` TEXT NOT NULL, FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE)")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_alarm_task_id` ON `alarm` (`task_id`)")
            db.execSQL("ALTER TABLE Task ADD COLUMN remind_task_at INTEGER NOT NULL DEFAULT $DEFAULT_TASK_REMINDER_MINUTES")
            db.execSQL("ALTER TABLE Task ADD COLUMN should_remind_task INTEGER NOT NULL DEFAULT false")
        }
    }
}