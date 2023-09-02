package com.zn.apps.domain.datastore

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.UserDataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@OptIn(ExperimentalCoroutinesApi::class)
class SetShouldHideOnboardingUseCaseTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val userDataRepository = mock<UserDataRepository>()
    private val useCase = SetShouldHideOnboardingUseCase(
        UseCase.Configuration(testDispatcher),
        userDataRepository
    )

    @Test
    fun `should set onboarding`() = runTest {
        val request = SetShouldHideOnboardingUseCase.Request(true)
        useCase.execute(request)
        verify(userDataRepository).setShouldHideOnboarding(true)
    }
}