package com.zn.apps.domain.util.filtering

import com.zn.apps.common.DeadlineType
import com.zn.apps.domain.util.TestUtils.taskResource_1_1
import com.zn.apps.domain.util.TestUtils.taskResource_1_2
import com.zn.apps.domain.util.TestUtils.taskResource_1_3
import com.zn.apps.domain.util.TestUtils.taskResource_1_4
import com.zn.apps.domain.util.TestUtils.taskResource_2_1
import com.zn.apps.domain.util.TestUtils.taskResource_2_2
import com.zn.apps.domain.util.TestUtils.taskResource_2_3
import com.zn.apps.domain.util.TestUtils.taskResource_2_4
import com.zn.apps.filter.Filter.DateFilter
import com.zn.apps.filter.Grouping.DeadlineTimeGrouping
import com.zn.apps.model.data.task.TaskResource
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateFilteringStrategyTest {

    private val list = listOf(
        taskResource_1_1,
        taskResource_1_2,
        taskResource_1_3,
        taskResource_1_4,
        taskResource_2_1,
        taskResource_2_2,
        taskResource_2_3,
        taskResource_2_4
    )

    @Test
    fun `given an empty list, return an empty list`() {
        val expected = emptyList<TaskResource>()
        val actual = DueDateFilteringStrategy(emptyList())
            .filter(DateFilter(DeadlineType.UPCOMING, DeadlineTimeGrouping))
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list, return items matching the filter`() {
        val expected = listOf(taskResource_1_3)
        val actual = DueDateFilteringStrategy(list).filter(
            DateFilter(
                DeadlineType.TOMORROW,
                DeadlineTimeGrouping
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list with one item, matching the filter return that list`() {
        val expected = listOf(taskResource_2_2)
        val actual = DueDateFilteringStrategy(listOf(taskResource_2_2))
            .filter(DateFilter(DeadlineType.UPCOMING, DeadlineTimeGrouping))
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list with one item not matching the filter, return an empty list`() {
        val expected = emptyList<TaskResource>()
        val actual = DueDateFilteringStrategy(listOf(taskResource_2_2))
            .filter(DateFilter(DeadlineType.TODAY, DeadlineTimeGrouping))
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list where no item match the filter, return an empty list`() {
        val expected = emptyList<TaskResource>()
        val actual = DueDateFilteringStrategy(listOf(taskResource_1_2, taskResource_1_3, taskResource_2_1))
            .filter(DateFilter(DeadlineType.SOMEDAY, DeadlineTimeGrouping))
        assertEquals(expected, actual)
    }
}