package com.zn.apps.domain.tag

import android.database.sqlite.SQLiteException
import com.zn.apps.domain.repository.TagRepository
import com.zn.apps.domain.util.TestUtils
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class DeleteTagUseCaseTest {

    private val tagRepository = mock<TagRepository>()
    private val useCase = DeleteTagUseCase(
        mock(),
        tagRepository
    )

    @Test
    fun `should delete a tag`() = runTest {
        val tag = TestUtils.sportTag
        val request = DeleteTagUseCase.Request(tag)
        useCase.process(request)
        verify(tagRepository).deleteTag(tag)
    }

    @Test
    fun `should throw an error when deleting a non-existent tag`() = runTest {
        val tag = TestUtils.sportTag
        val request = DeleteTagUseCase.Request(tag)
        doThrow(SQLiteException()).`when`(tagRepository).deleteTag(tag)
        try {
            useCase.process(request)
        } catch (e: Throwable) {
            Assert.assertTrue(e is UseCaseException.TagNotFoundException)
        }
    }
}