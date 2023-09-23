package com.zn.apps.ui_common.related_tasks

import com.zn.apps.common.MetadataResult
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult

data class RelatedTasksUiModel(
    val project: Project,
    val metadata: MetadataResult,
    val relatedTasksGrouped: Map<String, RelatedTasksMetaDataResult>
)
