package com.zn.apps.data_local.database.task

import androidx.room.Embedded
import androidx.room.Relation
import com.zn.apps.data_local.database.project.ProjectEntity
import com.zn.apps.data_local.database.tag.TagEntity

/**
 * Task entity with project and tag id references populated
 */
data class PopulatedTaskEntity(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "tag_id",
        entityColumn = "id"
    )
    val tag: List<TagEntity>,
    @Relation(
        parentColumn = "project_id",
        entityColumn = "id"
    )
    val project: List<ProjectEntity>
)
