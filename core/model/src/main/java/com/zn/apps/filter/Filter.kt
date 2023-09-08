package com.zn.apps.filter

import com.zn.apps.common.DeadlineType

sealed class Filter(open val grouping: Grouping) {
    /**
     * Filter tasks by tag
     *
     * @param filterId the id of the tag where all tasks' tag id will be compared to
     * @param grouping how to group those tasks with the same tag's id
     */
    data class TagFilter(val filterId: String, override val grouping: Grouping): Filter(grouping)

    /**
     * Filter tasks by date. [DateFilter] is only used once in the app.
     *
     * So we apply only the filter by [DeadlineType] and then group them
     */
    data class DateFilter(val dueDate: DeadlineType, override val grouping: Grouping): Filter(grouping)

    /**
     * Filter tasks that belongs to the same Project
     *
     * @param projectId the id that uniquely identifies the project in which those tasks belong to.
     * @param grouping how to group those related tasks
     */
    data class ProjectFilter(val projectId: String, override val grouping: Grouping): Filter(grouping)
}
