package com.zn.apps.domain.tag

import com.zn.apps.domain.repository.TagRepository
import com.zn.apps.domain.util.TestUtils.sportTag
import com.zn.apps.domain.util.TestUtils.studyTag
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SearchTagUseCaseTest {

    private val tagRepository = mock<TagRepository>()
    private val useCase = SearchTagUseCase(
        mock(),
        tagRepository
    )

    @Test
    fun `return an empty list when no match found`() = runTest {
        val query = "movie"
        val request = SearchTagUseCase.Request(query)
        whenever(tagRepository.searchTag(query)).thenReturn(flowOf(emptyList()))
        val response = useCase.process(request).first()
        assertEquals(SearchTagUseCase.Response(emptyList()), response)
    }

    @Test
    fun `return list of tag that match the query`() = runTest {
        val query = "s"
        val request = SearchTagUseCase.Request(query)
        whenever(tagRepository.searchTag(query)).thenReturn(flowOf(listOf(sportTag, studyTag)))
        val response = useCase.process(request).first()
        assertEquals(SearchTagUseCase.Response(listOf(sportTag, studyTag)), response)
    }
}