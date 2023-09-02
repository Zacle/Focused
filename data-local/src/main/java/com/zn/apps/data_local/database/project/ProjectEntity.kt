package com.zn.apps.data_local.database.project

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.zn.apps.data_local.database.tag.TagEntity
import java.time.OffsetDateTime

@Entity(
    tableName = "project",
    foreignKeys = [
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["tag_id"]), Index(value = ["name"], unique = true)]
)
data class ProjectEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "color")
    val color: Int,
    @ColumnInfo(name = "created_at")
    val createdAt: OffsetDateTime,
    @ColumnInfo(name = "tag_id")
    val tagId: String? = null,
    @ColumnInfo(name = "completed_time")
    val completedTime: OffsetDateTime? = null,
    @ColumnInfo(name = "completed")
    var completed: Boolean = false
) {

    /** Only IDs are used for equality. */
    override fun equals(other: Any?): Boolean = this === other || (other is ProjectEntity && other.id == id)

    /** Only IDs are used for equality. */
    override fun hashCode(): Int = id.hashCode()
}
