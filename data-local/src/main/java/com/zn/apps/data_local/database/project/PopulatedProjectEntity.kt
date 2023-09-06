package com.zn.apps.data_local.database.project

import androidx.room.Embedded
import androidx.room.Relation
import com.zn.apps.data_local.database.tag.TagEntity
import com.zn.apps.data_local.database.task.TaskEntity

/**
 * Project entity with its tasks
 */
data class PopulatedProjectEntity(
    @Embedded val project: ProjectEntity,
    @Relation(
        parentColumn = "tag_id",
        entityColumn = "id"
    )
    val tags: List<TagEntity>,
    @Relation(
        parentColumn = "id",
        entityColumn = "project_id"
    )
    val tasks: List<TaskEntity>
)
