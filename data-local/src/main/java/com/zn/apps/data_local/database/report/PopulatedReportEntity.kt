package com.zn.apps.data_local.database.report

import androidx.room.Embedded
import androidx.room.Relation
import com.zn.apps.data_local.database.project.ProjectEntity
import com.zn.apps.data_local.database.tag.TagEntity
import com.zn.apps.data_local.database.task.TaskEntity

data class PopulatedReportEntity(
    @Embedded val report: ReportEntity,
    @Relation(
        parentColumn = "task_id",
        entityColumn = "id"
    )
    val task: List<TaskEntity>,
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
