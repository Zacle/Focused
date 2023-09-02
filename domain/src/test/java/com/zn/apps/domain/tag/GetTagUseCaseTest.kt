package com.zn.apps.domain.tag

import com.zn.apps.domain.repository.TagRepository
import com.zn.apps.domain.util.TestUtils
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetTagUseCaseTest {

    private val tagRepository = mock<TagRepository>()
    private val useCase = GetTagUseCase(
        mock(),
        tagRepository
    )

    @Test
    fun `should retrieve a tag`() = runTest {
        val tag = TestUtils.sportTag
        val request = GetTagUseCase.Request(tag.id)
        whenever(tagRepository.getTag(request.tagId)).thenReturn(flowOf(tag))
        val response = useCase.process(request).first()
        assertEquals(GetTagUseCase.Response(tag), response)
    }

    @Test
    fun `throws an exception if tag id not found`() = runTest {
        val tag = TestUtils.studyTag
        val request = GetTagUseCase.Request(tag.id)
        whenever(tagRepository.getTag("no tag")).thenReturn(flowOf(null))
        try {
            useCase.process(request)
        } catch (e: Throwable) {
            Assert.assertTrue(e is UseCaseException.TagNotFoundException)
        }
    }
}