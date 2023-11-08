package com.zn.apps.domain.alarm

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.AlarmRepository
import com.zn.apps.model.data.alarm.AlarmItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAlarmsUseCase(
    configuration: Configuration,
    private val alarmRepository: AlarmRepository
): UseCase<GetAlarmsUseCase.Request, GetAlarmsUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        alarmRepository.getAlarms().map { Response(it) }

    data object Request: UseCase.Request

    data class Response(val alarms: List<AlarmItem>): UseCase.Response
}