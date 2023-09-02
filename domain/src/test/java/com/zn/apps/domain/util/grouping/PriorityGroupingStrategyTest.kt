package com.zn.apps.domain.util.grouping

import com.zn.apps.domain.util.TestUtils.taskResource_1_1
import com.zn.apps.domain.util.TestUtils.taskResource_1_2
import com.zn.apps.domain.util.TestUtils.taskResource_1_3
import com.zn.apps.domain.util.TestUtils.taskResource_1_4
import com.zn.apps.domain.util.TestUtils.taskResource_2_1
import com.zn.apps.domain.util.TestUtils.taskResource_2_2
import com.zn.apps.domain.util.TestUtils.taskResource_2_3
import com.zn.apps.domain.util.TestUtils.taskResource_2_4
import com.zn.apps.model.data.task.TaskPriority
import com.zn.apps.model.data.task.TaskResource
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class PriorityGroupingStrategyTest {

    @Test
    fun `given an empty list of tasks, return an empty map`() {
        val expected = emptyMap<String, List<TaskResource>>()
        val actual = PriorityGroupingStrategy(emptyList()).group()
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list with one item, return a map with one key`() {
        val list = listOf(taskResource_1_3)
        val result = PriorityGroupingStrategy(list).group()
        assertEquals(1, result.size)
        Assert.assertTrue(result.containsKey(TaskPriority.MEDIUM.name))
        assertEquals(listOf(taskResource_1_3), result[TaskPriority.MEDIUM.name])
    }

    @Test
    fun `given a list with two items related, return a map with one key`() {
        val list = listOf(taskResource_1_1, taskResource_1_2)
        val result = PriorityGroupingStrategy(list).group()
        assertEquals(1, result.size)
        Assert.assertTrue(result.containsKey(TaskPriority.NONE.name))
        assertEquals(list, result[TaskPriority.NONE.name])
    }

    @Test
    fun `given a list with two keys not related, return a map with two keys`() {
        val list = listOf(taskResource_1_3, taskResource_1_4)
        val result = PriorityGroupingStrategy(list).group()
        assertEquals(2, result.size)
        Assert.assertTrue(result.containsKey(TaskPriority.HIGH.name))
        Assert.assertTrue(result.containsKey(TaskPriority.MEDIUM.name))
        assertEquals(listOf(taskResource_1_3), result[TaskPriority.MEDIUM.name])
        assertEquals(listOf(taskResource_1_4), result[TaskPriority.HIGH.name])
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
        val result = PriorityGroupingStrategy(list).group()
        assertEquals(4, result.size)
        assertEquals(listOf(taskResource_2_1), result[TaskPriority.LOW.name])
    }
}