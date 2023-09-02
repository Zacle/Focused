package com.zn.apps.domain.tag

import com.zn.apps.domain.repository.TagRepository
import com.zn.apps.domain.util.TestUtils.sportTag
import com.zn.apps.domain.util.TestUtils.studyTag
import com.zn.apps.domain.util.TestUtils.workTag
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetTagsUseCaseTest {

    private val tagRepository = mock<TagRepository>()
    private val useCase = GetTagsUseCase(
        mock(),
        tagRepository
    )

    @Test
    fun `should return all tags`() = runTest {
        val list = listOf(sportTag, workTag, studyTag)
        val request = GetTagsUseCase.Request
        whenever(tagRepository.getTags()).thenReturn(flowOf(list))
        val response = useCase.process(request).first()
        assertEquals(GetTagsUseCase.Response(list), response)
    }
}