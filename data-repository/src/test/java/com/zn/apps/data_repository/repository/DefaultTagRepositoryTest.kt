package com.zn.apps.data_repository.repository

import com.zn.apps.data_repository.data_source.local.TagDataSource
import com.zn.apps.data_repository.util.TestUtils.sportTag
import com.zn.apps.data_repository.util.TestUtils.studyTag
import com.zn.apps.data_repository.util.TestUtils.workTag
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DefaultTagRepositoryTest {

    private val tagDataSource = mock<TagDataSource>()
    private val repository = DefaultTagRepository(tagDataSource)

    @Test
    fun testGetTags() = runTest {
        val list = listOf(studyTag, sportTag, workTag)
        whenever(tagDataSource.getTags()).thenReturn(flowOf(list))
        val response = repository.getTags().first()
        assertEquals(list, response)
        verify(tagDataSource).getTags()
    }

    @Test
    fun testGetTag() = runTest {
        val tag = sportTag
        whenever(tagDataSource.getTag(sportTag.id)).thenReturn(flowOf(tag))
        val response = repository.getTag(tag.id).first()
        assertEquals(tag, response)
        verify(tagDataSource).getTag(tag.id)
    }

    @Test
    fun testUpsertTag() = runTest {
        val tag = studyTag
        repository.upsertTag(tag)
        verify(tagDataSource).upsertTag(tag)
    }

    @Test
    fun testDeleteTag() = runTest {
        val tag = workTag
        repository.deleteTag(tag)
        verify(tagDataSource).deleteTag(tag)
    }

    @Test
    fun testSearchTag() = runTest {
        val tags = listOf(sportTag, studyTag)
        whenever(tagDataSource.searchTag("s")).thenReturn(flowOf(tags))
        val result = repository.searchTag("s").first()
        assertEquals(tags, result)
        verify(tagDataSource).searchTag("s")
    }
}