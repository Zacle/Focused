package com.zn.apps.data_local.model

import com.zn.apps.data_local.database.project.ProjectEntity
import com.zn.apps.model.data.project.Project

fun ProjectEntity.asExternalModel() =
    Project(id, name, color, createdAt, tagId, completedTime, completed)

fun Project.asEntity() =
    ProjectEntity(id, name, color, createdAt, tagId, completedTime, completed)