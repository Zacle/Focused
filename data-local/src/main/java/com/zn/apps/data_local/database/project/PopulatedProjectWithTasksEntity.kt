package com.zn.apps.data_local.database.project

import androidx.room.Embedded
import androidx.room.Relation
import com.zn.apps.data_local.database.task.PopulatedTaskEntity
import com.zn.apps.data_local.database.task.TaskEntity

data class PopulatedProjectWithTasksEntity(
    @Embedded val project: ProjectEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "project_id",
        entity = TaskEntity::class
    )
    val taskResources: List<PopulatedTaskEntity>
)
