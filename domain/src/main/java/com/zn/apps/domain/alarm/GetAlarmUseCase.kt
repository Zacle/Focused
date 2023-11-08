package com.zn.apps.domain.alarm

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.AlarmRepository
import com.zn.apps.model.data.alarm.AlarmItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAlarmUseCase(
    configuration: Configuration,
    private val alarmRepository: AlarmRepository
): UseCase<GetAlarmUseCase.Request, GetAlarmUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        alarmRepository.getAlarm(request.taskId).map { Response(it) }

    data class Request(val taskId: String): UseCase.Request

    data class Response(val alarmItem: AlarmItem?): UseCase.Response
}