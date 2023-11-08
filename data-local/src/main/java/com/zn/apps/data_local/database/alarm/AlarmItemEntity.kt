package com.zn.apps.data_local.database.alarm

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zn.apps.data_local.database.task.TaskEntity
import java.time.OffsetDateTime

@Entity(
    tableName = "alarm",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"]
        )
    ],
    indices = [Index(value = ["task_id"])]
)
data class AlarmItemEntity(
    @ColumnInfo(name = "task_id") var taskId: String,
    @ColumnInfo(name = "remind_at") var remindAt: OffsetDateTime
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
