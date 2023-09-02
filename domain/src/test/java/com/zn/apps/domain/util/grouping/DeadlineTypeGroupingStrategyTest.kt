package com.zn.apps.domain.util.grouping

import com.zn.apps.common.DeadlineType
import com.zn.apps.domain.util.TestUtils.taskResource_1_1
import com.zn.apps.domain.util.TestUtils.taskResource_1_2
import com.zn.apps.domain.util.TestUtils.taskResource_1_3
import com.zn.apps.domain.util.TestUtils.taskResource_1_4
import com.zn.apps.domain.util.TestUtils.taskResource_2_1
import com.zn.apps.domain.util.TestUtils.taskResource_2_2
import com.zn.apps.domain.util.TestUtils.taskResource_2_3
import com.zn.apps.domain.util.TestUtils.taskResource_2_4
import com.zn.apps.model.data.task.TaskResource
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class DeadlineTypeGroupingStrategyTest {

    @Test
    fun `given an empty list of tasks, return an empty map`() {
        val expected = emptyMap<String, List<TaskResource>>()
        val actual = DeadlineTypeGroupingStrategy(emptyList()).group()
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list with one item, return a map with one key`() {
        val list = listOf(taskResource_1_3)
        val result = DeadlineTypeGroupingStrategy(list).group()
        assertEquals(1, result.size)
        Assert.assertTrue(result.containsKey(DeadlineType.TOMORROW.name))
        assertEquals(list, result[DeadlineType.TOMORROW.name])
    }

    @Test
    fun `given a list with two items related, return a map with one key`() {
        val list = listOf(taskResource_1_1, taskResource_1_4)
        val result = DeadlineTypeGroupingStrategy(list).group()
        assertEquals(1, result.size)
        Assert.assertTrue(result.containsKey(DeadlineType.SOMEDAY.name))
        assertEquals(listOf(taskResource_1_1, taskResource_1_4), result[DeadlineType.SOMEDAY.name])
    }

    @Test
    fun `given a list with two keys not related, return a map with two keys`() {
        val list = listOf(taskResource_1_2, taskResource_1_3)
        val result = DeadlineTypeGroupingStrategy(list).group()
        assertEquals(2, result.size)
        Assert.assertTrue(result.containsKey(DeadlineType.TODAY.name))
        Assert.assertTrue(result.containsKey(DeadlineType.TOMORROW.name))
        assertEquals(listOf(taskResource_1_2), result[DeadlineType.TODAY.name])
        assertEquals(listOf(taskResource_1_3), result[DeadlineType.TOMORROW.name])
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
            taskResource_2_3,
            taskResource_2_4
        )
        val result = DeadlineTypeGroupingStrategy(list).group()
        assertEquals(4, result.size)
        assertEquals(listOf(taskResource_2_2, taskResource_2_4), result[DeadlineType.UPCOMING.name])
    }
}