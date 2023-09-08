package com.zn.apps.domain.util.filtering

import com.zn.apps.domain.util.TestUtils.project_1
import com.zn.apps.domain.util.TestUtils.project_2
import com.zn.apps.domain.util.TestUtils.taskResource_1_1
import com.zn.apps.domain.util.TestUtils.taskResource_1_2
import com.zn.apps.domain.util.TestUtils.taskResource_1_3
import com.zn.apps.domain.util.TestUtils.taskResource_1_4
import com.zn.apps.filter.Filter.ProjectFilter
import com.zn.apps.filter.Grouping.TagGrouping
import com.zn.apps.model.data.task.TaskResource
import org.junit.Assert.assertEquals
import org.junit.Test

class ProjectFilteringStrategyTest {

    @Test
    fun `given an empty list, return an empty list`() {
        val expected = emptyList<TaskResource>()
        val actual = ProjectFilteringStrategy(emptyList()).filter(
            ProjectFilter(
                projectId = project_1.id,
                grouping = TagGrouping
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list with one item, matching the filter return that list`() {
        val expected = listOf(taskResource_1_2)
        val actual = ProjectFilteringStrategy(listOf(taskResource_1_2)).filter(
            ProjectFilter(
                projectId = project_1.id,
                grouping = TagGrouping
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list with one item not matching the filter, return an empty list`() {
        val expected = emptyList<TaskResource>()
        val actual = ProjectFilteringStrategy(listOf(taskResource_1_2)).filter(
            ProjectFilter(
                projectId = project_2.id,
                grouping = TagGrouping
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list, return items matching the filter`() {
        val list = listOf(taskResource_1_1, taskResource_1_2, taskResource_1_3, taskResource_1_4)
        val expected = listOf(taskResource_1_2, taskResource_1_3)
        val actual = ProjectFilteringStrategy(list).filter(
            ProjectFilter(
                projectId = project_1.id,
                grouping = TagGrouping
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list where no item match the filter, return an empty list`() {
        val list = listOf(taskResource_1_1, taskResource_1_2, taskResource_1_3, taskResource_1_4)
        val expected = emptyList<TaskResource>()
        val actual = ProjectFilteringStrategy(list).filter(
            ProjectFilter(
                projectId = project_2.id,
                grouping = TagGrouping
            )
        )
        assertEquals(expected, actual)
    }
}