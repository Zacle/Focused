package com.zn.apps.domain.alarm

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DeleteAlarmUseCase(
    configuration: Configuration,
    private val alarmRepository: AlarmRepository
): UseCase<DeleteAlarmUseCase.Request, DeleteAlarmUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        alarmRepository.deleteAlarm(request.taskId)
        return flowOf(Response)
    }

    data class Request(val taskId: String): UseCase.Request

    data object Response: UseCase.Response
}