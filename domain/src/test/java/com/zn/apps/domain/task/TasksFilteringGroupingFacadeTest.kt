package com.zn.apps.domain.task

import com.zn.apps.common.DeadlineType
import com.zn.apps.domain.RelatedTasksUseCase
import com.zn.apps.domain.util.TestUtils.sportTag
import com.zn.apps.domain.util.TestUtils.taskResource_1_1
import com.zn.apps.domain.util.TestUtils.taskResource_1_2
import com.zn.apps.domain.util.TestUtils.taskResource_1_3
import com.zn.apps.domain.util.TestUtils.taskResource_1_4
import com.zn.apps.domain.util.TestUtils.taskResource_2_1
import com.zn.apps.domain.util.TestUtils.workTag
import com.zn.apps.filter.Filter
import com.zn.apps.filter.Grouping
import com.zn.apps.filter.Grouping.DeadlineTypeGrouping
import com.zn.apps.filter.Grouping.PriorityGrouping
import com.zn.apps.model.data.task.RelatedTasksMetaDataResult
import com.zn.apps.model.data.task.TaskPriority
import com.zn.apps.model.data.task.TaskResource
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TasksFilteringGroupingFacadeTest {

    private lateinit var relatedTasksUseCase: RelatedTasksUseCase

    @Before
    fun setup() {
        relatedTasksUseCase = RelatedTasksUseCase()
    }

    @Test
    fun `given an empty list return an empty map`() {
        val list = emptyList<TaskResource>()
        val tasksFilteringGroupingFacade = TasksFilteringGroupingFacade(
            taskResources = list,
            filter = Filter.TagFilter("", DeadlineTypeGrouping),
            filterCompletedProject = false
        )
        assertEquals(emptyMap<String, RelatedTasksMetaDataResult>(), tasksFilteringGroupingFacade.execute())
    }

    @Test
    fun `given a list with one element return a map of one element`() {
        val list = listOf(taskResource_1_2)
        val tasksFilteringGroupingFacade = TasksFilteringGroupingFacade(
            taskResources = list,
            filter = Filter.TagFilter(workTag.id, PriorityGrouping),
            filterCompletedProject = false
        )
        val result = tasksFilteringGroupingFacade.execute()
        val metaDataResult = relatedTasksUseCase(list)
        assertEquals(mapOf(TaskPriority.NONE.name to metaDataResult), result)
    }

    @Test
    fun `given a list with one element with project completed return an empty map`() {
        val list = listOf(taskResource_1_2)
        val tasksFilteringGroupingFacade = TasksFilteringGroupingFacade(
            taskResources = list,
            filter = Filter.TagFilter(workTag.id, PriorityGrouping),
            filterCompletedProject = true
        )
        val result = tasksFilteringGroupingFacade.execute()
        assertEquals(emptyMap<String, RelatedTasksMetaDataResult>(), result)
    }

    @Test
    fun `return a map of elements matching the filter and grouping`() {
        val list = listOf(taskResource_1_1, taskResource_1_3, taskResource_1_4, taskResource_2_1)
        val tasksFilteringGroupingFacade = TasksFilteringGroupingFacade(
            taskResources = list,
            filter = Filter.DateFilter(DeadlineType.SOMEDAY, Grouping.TagGrouping),
            filterCompletedProject = true
        )
        val actual = tasksFilteringGroupingFacade.execute()
        val expected = mapOf(
            sportTag.getDisplayName() to relatedTasksUseCase(listOf(taskResource_1_1)),
            "" to relatedTasksUseCase(listOf(taskResource_1_4))
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `given an empty filter id, return all tasks then group them`() {
        val list = listOf(taskResource_1_1, taskResource_1_3, taskResource_1_4, taskResource_2_1)
        val tasksFilteringGroupingFacade = TasksFilteringGroupingFacade(
            taskResources = list,
            filter = Filter.TagFilter("", DeadlineTypeGrouping),
            filterCompletedProject = true
        )
        val actual = tasksFilteringGroupingFacade.execute()
        val expected = mapOf(
            DeadlineType.TODAY.name to relatedTasksUseCase(listOf(taskResource_2_1)),
            DeadlineType.TOMORROW.name to relatedTasksUseCase(listOf(taskResource_1_3)),
            DeadlineType.SOMEDAY.name to relatedTasksUseCase(listOf(taskResource_1_1, taskResource_1_4))
        )
        assertEquals(expected, actual)
    }
}