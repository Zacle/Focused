package com.zn.apps.ui_common.related_tasks

import com.zn.apps.common.MetadataResult
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult

data class RelatedTasksUiModel(
    val screenTitle: String,
    val metadata: MetadataResult,
    val relatedTasksGrouped: Map<String, RelatedTasksMetaDataResult>
)
