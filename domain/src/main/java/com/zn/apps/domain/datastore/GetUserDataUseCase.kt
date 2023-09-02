package com.zn.apps.domain.datastore

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.UserDataRepository
import com.zn.apps.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUserDataUseCase(
    configuration: Configuration,
    private val userDataRepository: UserDataRepository
): UseCase<GetUserDataUseCase.Request, GetUserDataUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        return userDataRepository.userData.map { Response(it) }
    }

    object Request: UseCase.Request

    data class Response(val userData: UserData): UseCase.Response
}