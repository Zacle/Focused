package com.zn.apps.feature.projects.list

import com.zn.apps.model.data.project.ProjectResource

data class ProjectsUiModel(
    val projectResources: List<ProjectResource> = emptyList()
)
