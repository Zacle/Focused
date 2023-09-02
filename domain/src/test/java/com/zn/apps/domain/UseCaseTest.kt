package com.zn.apps.domain

import com.zn.apps.model.usecase.Result.*
import com.zn.apps.model.usecase.UseCaseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class UseCaseTest {
    private val configuration = UseCase.Configuration(UnconfinedTestDispatcher())
    private val request = mock<UseCase.Request>()
    private val response = mock<UseCase.Response>()

    private lateinit var useCase: UseCase<UseCase.Request, UseCase.Response>

    @Before
    fun setUp() {
        useCase = object : UseCase<UseCase.Request, UseCase.Response>(configuration) {
            override suspend fun process(request: Request): Flow<Response> {
                assertEquals(this@UseCaseTest.request, request)
                return flowOf(response)
            }

        }
    }

    @Test
    fun `test execute return success`() = runTest {
        val result = useCase.execute(request).first()
        assertEquals(Success(response), result)
    }

    @Test
    fun `should return unknown error`() = runTest {
        useCase = object : UseCase<UseCase.Request, UseCase.Response>(configuration) {
            override suspend fun process(request: Request): Flow<Response> {
                assertEquals(this@UseCaseTest.request, request)
                return flow {
                    throw Throwable()
                }
            }
        }
        val result = useCase.execute(request).first()
        Assert.assertTrue((result as Error).exception is UseCaseException.UnknownException)
    }

    @Test
    fun `should return task not found exception`() = runTest {
        useCase = object : UseCase<UseCase.Request, UseCase.Response>(configuration) {
            override suspend fun process(request: Request): Flow<Response> {
                assertEquals(this@UseCaseTest.request, request)
                return flow {
                    throw UseCaseException.TaskNotFoundException(Throwable())
                }
            }
        }
        val result = useCase.execute(request).first()
        Assert.assertTrue((result as Error).exception is UseCaseException.TaskNotFoundException)
    }

    @Test
    fun `should return task not updated exception`() = runTest {
        useCase = object : UseCase<UseCase.Request, UseCase.Response>(configuration) {
            override suspend fun process(request: Request): Flow<Response> {
                assertEquals(this@UseCaseTest.request, request)
                return flow {
                    throw UseCaseException.TaskNotUpdatedException(Throwable())
                }
            }
        }
        val result = useCase.execute(request).first()
        Assert.assertTrue((result as Error).exception is UseCaseException.TaskNotUpdatedException)
    }
}