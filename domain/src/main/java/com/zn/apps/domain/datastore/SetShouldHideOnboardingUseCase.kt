package com.zn.apps.domain.datastore

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.UserDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SetShouldHideOnboardingUseCase(
    configuration: Configuration,
    private val userDataRepository: UserDataRepository
): UseCase<SetShouldHideOnboardingUseCase.Request, SetShouldHideOnboardingUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        userDataRepository.setShouldHideOnboarding(request.shouldHideOnboarding)
        return flowOf(Response)
    }

    data class Request(val shouldHideOnboarding: Boolean): UseCase.Request

    object Response: UseCase.Response
}