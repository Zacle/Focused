package com.zn.apps.feature.tasks.list

import com.zn.apps.model.data.tag.Tag
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult

data class TasksUiModel(
    val tags: List<Tag> = emptyList(),
    val groupedTasks: Map<String, RelatedTasksMetaDataResult> = emptyMap()
)
