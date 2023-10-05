package com.zn.apps.data_local.database.report

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.SET_NULL
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zn.apps.data_local.database.project.ProjectEntity
import com.zn.apps.data_local.database.tag.TagEntity
import com.zn.apps.data_local.database.task.TaskEntity
import java.time.OffsetDateTime

@Entity(
    tableName = "report",
    foreignKeys = [
        ForeignKey(
            entity = TaskEntity::class,
            parentColumns = ["id"],
            childColumns = ["task_id"],
            onDelete = SET_NULL
        ),
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = SET_NULL
        ),
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["project_id"],
            onDelete = SET_NULL
        )
    ],
    indices = [Index(value = ["task_id"]), Index(value = ["tag_id"]), Index(value = ["project_id"])]
)
data class ReportEntity(
    @ColumnInfo(name = "completed_time") val completedTime: OffsetDateTime,
    @ColumnInfo(name = "elapsed_time") var elapsedTime: Long,
    @ColumnInfo(name = "task_id") var taskId: String? = null,
    @ColumnInfo(name = "tag_id") var tagId: String? = null,
    @ColumnInfo(name = "project_id") var projectId: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
