package com.zn.apps.domain.datastore

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.PomodoroStateRepository
import com.zn.apps.model.data.pomodoro.PomodoroState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPomodoroStateUseCase(
    configuration: Configuration,
    private val pomodoroStateRepository: PomodoroStateRepository
): UseCase<GetPomodoroStateUseCase.Request, GetPomodoroStateUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        pomodoroStateRepository.pomodoroState.map { Response(it) }

    data object Request: UseCase.Request

    data class Response(val pomodoroState: PomodoroState): UseCase.Response
}