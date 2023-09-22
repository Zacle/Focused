package com.zn.apps.model.data.project

import com.zn.apps.model.data.task.TaskResource

data class PopulatedProjectResource(
    val project: Project,
    val taskResources: List<TaskResource>
)