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

class UpsertTagUseCaseTest {

    private val tagRepository = mock<TagRepository>()
    private val useCase = UpsertTagUseCase(
        mock(),
        tagRepository
    )

    @Test
    fun `should add or modify a tag`() = runTest {
        val tag = TestUtils.studyTag
        val request = UpsertTagUseCase.Request(tag)
        useCase.process(request)
        verify(tagRepository).upsertTag(tag)
    }

    @Test
    fun `adding or modifying a non existent tag should throw a task not found exception`() = runTest {
        val tag = TestUtils.workTag
        val request = UpsertTagUseCase.Request(tag)
        doThrow(SQLiteException()).`when`(tagRepository).upsertTag(tag)
        try {
            useCase.process(request)
        } catch (e: Throwable) {
            Assert.assertTrue(e is UseCaseException.TagNotUpdatedException)
        }
    }
}