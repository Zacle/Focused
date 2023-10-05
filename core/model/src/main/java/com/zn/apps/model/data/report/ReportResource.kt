package com.zn.apps.model.data.report

import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.model.data.task.Task
import java.time.OffsetDateTime

data class ReportResource(
    val completedTime: OffsetDateTime,
    val elapsedTime: Long,
    val task: Task? = null,
    val tag: Tag? = null,
    val project: Project? = null
)
