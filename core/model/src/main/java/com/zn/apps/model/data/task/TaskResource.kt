package com.zn.apps.model.data.task

/**
 * External data layer representation of a fully populated Focused tasks resource
 *
 * @param task the task associated to the resource
 * @param projectId the project id if this task belongs to a project
 * @param projectName the project name if this task belongs to a project
 * @param projectCompleted whether or not the project in which this task is completed
 * @param tagId the tag id if theis task has a tag
 * @param tagName the tag name if this task has a tag
 */
data class TaskResource(
    val task: Task,
    val projectId: String? = null,
    val projectName: String = "",
    val projectCompleted: Boolean = false,
    val tagId: String? = null,
    val tagName: String = ""
)
