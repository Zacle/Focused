package com.zn.apps.domain.util.filtering

import com.zn.apps.domain.util.TestUtils.sportTag
import com.zn.apps.domain.util.TestUtils.studyTag
import com.zn.apps.domain.util.TestUtils.taskResource_1_1
import com.zn.apps.domain.util.TestUtils.taskResource_1_2
import com.zn.apps.domain.util.TestUtils.taskResource_1_3
import com.zn.apps.domain.util.TestUtils.taskResource_1_4
import com.zn.apps.domain.util.TestUtils.taskResource_2_1
import com.zn.apps.domain.util.TestUtils.taskResource_2_2
import com.zn.apps.domain.util.TestUtils.taskResource_2_3
import com.zn.apps.domain.util.TestUtils.taskResource_2_4
import com.zn.apps.domain.util.TestUtils.workTag
import com.zn.apps.filter.Filter.TagFilter
import com.zn.apps.filter.Grouping.TagGrouping
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.model.data.task.TaskResource
import org.junit.Assert.assertEquals
import org.junit.Test

class TagFilteringStrategyTest {

    @Test
    fun `given an empty list, return an empty list`() {
        val expected = emptyList<TaskResource>()
        val actual = TagFilteringStrategy(emptyList()).filter(
            TagFilter(
                workTag.id,
                TagGrouping
            )
        )
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list with one item, matching the filter return that list`() {
        val expected = listOf(taskResource_1_2)
        val actual = TagFilteringStrategy(listOf(taskResource_1_2))
            .filter(TagFilter(workTag.id, TagGrouping))
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list with one item not matching the filter, return an empty list`() {
        val expected = emptyList<TaskResource>()
        val actual = TagFilteringStrategy(listOf(taskResource_1_2))
            .filter(TagFilter(sportTag.id, TagGrouping))
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list, return items matching the filter`() {
        val list = listOf(
            taskResource_1_1, taskResource_1_2, taskResource_1_3, taskResource_1_4,
            taskResource_2_1, taskResource_2_2, taskResource_2_3, taskResource_2_4
        )
        val expected = listOf(taskResource_1_3, taskResource_2_4)
        val actual = TagFilteringStrategy(list).filter(TagFilter(studyTag.id, TagGrouping))
        assertEquals(expected, actual)
    }

    @Test
    fun `given a list where no item match the filter, return an empty list`() {
        val list = listOf(
            taskResource_1_1, taskResource_1_2, taskResource_1_3, taskResource_1_4,
            taskResource_2_1, taskResource_2_2, taskResource_2_3, taskResource_2_4
        )
        val expected = emptyList<TaskResource>()
        val actual = TagFilteringStrategy(list).filter(TagFilter(Tag(name = "default").id, TagGrouping))
        assertEquals(expected, actual)
    }
}