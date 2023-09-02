package com.zn.apps.domain.util.grouping

import com.zn.apps.common.DeadlineTime
import com.zn.apps.domain.util.TestUtils.taskResource_1_1
import com.zn.apps.domain.util.TestUtils.taskResource_1_2
import com.zn.apps.domain.util.TestUtils.taskResource_1_3
import com.zn.apps.domain.util.TestUtils.taskResource_1_4
import com.zn.apps.domain.util.TestUtils.taskResource_2_1
import com.zn.apps.domain.util.TestUtils.taskResource_2_2
import com.zn.apps.domain.util.TestUtils.taskResource_2_3
import com.zn.apps.domain.util.TestUtils.taskResource_2_5
import com.zn.apps.model.data.task.TaskResource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DeadlineTimeGroupingStrategyTest {

    @Test
    fun `given an empty list of tasks, return an empty map`() {
        val expected = emptyMap<String, List<TaskResource>>()
        val actual = DeadlineTimeGroupingStrategy(emptyList()).group()
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list with one item, return a map with one key`() {
        val list = listOf(taskResource_1_1)
        val result = DeadlineTimeGroupingStrategy(list).group()
        assertEquals(1, result.size)
        assertTrue(result.containsKey(""))
        assertEquals(list, result[""])
    }

    @Test
    fun `given a list with two items related, return a map with one key`() {
        val list = listOf(taskResource_2_2, taskResource_2_5)
        val result = DeadlineTimeGroupingStrategy(list).group()
        assertEquals(1, result.size)
        assertTrue(result.containsKey(DeadlineTime.OTHER.name))
        assertEquals(list, result[DeadlineTime.OTHER.name])
    }

    @Test
    fun `given a list with two keys not related, return a map with two keys`() {
        val list = listOf(taskResource_1_2, taskResource_2_1)
        val result = DeadlineTimeGroupingStrategy(list).group()
        assertEquals(2, result.size)
        assertTrue(result.containsKey(DeadlineTime.TODAY.name))
        assertTrue(result.containsKey(DeadlineTime.YESTERDAY.name))
        assertEquals(listOf(taskResource_1_2), result[DeadlineTime.TODAY.name])
        assertEquals(listOf(taskResource_2_1), result[DeadlineTime.YESTERDAY.name])
    }

    @Test
    fun `given a list a list with many items, return a map with related items`() {
        val list = listOf(
            taskResource_1_1,
            taskResource_1_2,
            taskResource_1_3,
            taskResource_1_4,
            taskResource_2_1,
            taskResource_2_2,
            taskResource_2_3
        )
        val result = DeadlineTimeGroupingStrategy(list).group()
        assertEquals(5, result.size)
        assertEquals(listOf(taskResource_1_1, taskResource_1_4, taskResource_2_3), result[""])
    }
}