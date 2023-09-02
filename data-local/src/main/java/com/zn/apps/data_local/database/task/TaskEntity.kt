package com.zn.apps.data_local.database.task

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.ForeignKey.Companion.SET_NULL
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zn.apps.data_local.database.project.ProjectEntity
import com.zn.apps.data_local.database.tag.TagEntity
import com.zn.apps.model.data.task.Pomodoro
import com.zn.apps.model.data.task.TaskPriority
import java.time.OffsetDateTime

@Entity(
    tableName = "task",
    foreignKeys = [
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
            onDelete = CASCADE
        )
    ],
    indices = [Index(value = ["project_id"]), Index(value = ["tag_id"])]
)
data class TaskEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @Embedded
    val pomodoro: Pomodoro,
    @ColumnInfo(name = "priority")
    val priority: TaskPriority,
    @ColumnInfo(name = "project_id")
    val projectId: String? = null,
    @ColumnInfo(name = "tag_id")
    val tagId: String? = null,
    @ColumnInfo(name = "due_date")
    val dueDate: OffsetDateTime? = null,
    @ColumnInfo(name = "completed_time")
    val completedTime: OffsetDateTime? = null,
    @ColumnInfo(name = "completed")
    var completed: Boolean = false
) {
    /** Only IDs are used for equality. */
    override fun equals(other: Any?): Boolean = this === other || (other is TaskEntity && other.id == id)

    /** Only IDs are used for equality. */
    override fun hashCode(): Int = id.hashCode()
}