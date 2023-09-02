package com.zn.apps.domain.task

import com.zn.apps.common.DeadlineType
import com.zn.apps.domain.RelatedTasksUseCase
import com.zn.apps.domain.repository.TaskRepository
import com.zn.apps.domain.util.TestUtils
import com.zn.apps.filter.Filter
import com.zn.apps.filter.Grouping.TagGrouping
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetTasksUseCaseTest {

    private val taskRepository = mock<TaskRepository>()

    private val useCase = GetTasksUseCase(
        mock(),
        taskRepository
    )

    @Test
    fun testGetTasks() = runTest {
        val list = listOf(
            TestUtils.taskResource_1_1,
            TestUtils.taskResource_1_3,
            TestUtils.taskResource_1_4,
            TestUtils.taskResource_2_1
        )
        val request = GetTasksUseCase.Request(
            filter = Filter.DateFilter(DeadlineType.SOMEDAY, TagGrouping),
            taskCompleted = false,
            projectCompleted = false
        )
        whenever(taskRepository.getTaskResources(false)).thenReturn(flowOf(list))
        val response = useCase.process(request).first()
        assertEquals(
            GetTasksUseCase.Response(
                mapOf(
                    TestUtils.sportTag.getDisplayName() to RelatedTasksUseCase()(listOf(TestUtils.taskResource_1_1)),
                    "" to RelatedTasksUseCase()(listOf(TestUtils.taskResource_1_4))
                )
            ),
            response
        )
    }
}