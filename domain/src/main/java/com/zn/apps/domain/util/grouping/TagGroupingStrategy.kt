package com.zn.apps.domain.util.grouping

import android.content.Context
import com.zn.apps.model.R
import com.zn.apps.model.data.task.TaskResource

class TagGroupingStrategy(
    private val context: Context,
    private val taskResources: List<TaskResource>
): GroupingStrategy {
    override fun group(): Map<String, List<TaskResource>> {
        val tagGroup = mutableMapOf<String, MutableList<TaskResource>>()
        taskResources.forEach { taskResource ->
            tagGroup.getOrPut(getTagName(context, taskResource.tagName)) { mutableListOf() }.add(taskResource)
        }
        return tagGroup
    }
}

fun getTagName(context: Context, tagName: String) =
    tagName.ifEmpty { context.getString(R.string.no_tag) }