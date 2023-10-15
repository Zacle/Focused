package com.zn.apps.domain.datastore

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.PomodoroPreferencesRepository
import com.zn.apps.model.datastore.PomodoroPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPomodoroPreferencesUseCase(
    configuration: Configuration,
    private val pomodoroPreferencesRepository: PomodoroPreferencesRepository
): UseCase<GetPomodoroPreferencesUseCase.Request, GetPomodoroPreferencesUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        pomodoroPreferencesRepository.pomodoroPreferences.map { Response(it) }

    data object Request: UseCase.Request

    data class Response(val pomodoroPreferences: PomodoroPreferences): UseCase.Response
}