package com.zn.apps.model.data.project

/**
 * External data layer representation of a fully populated Focused tasks resource
 *
 * @param project the project associated to the resource
 * @param numberOfTasks number of tasks in the [Project]
 * @param numberOfTasksCompleted number of tasks completed
 * @param tagName the tag name of the project
 */
data class ProjectResource(
    val project: Project,
    val numberOfTasks: Int,
    val numberOfTasksCompleted: Int,
    val tagName: String
)
