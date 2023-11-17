package com.zn.apps.domain.alarm

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.AlarmRepository
import com.zn.apps.model.data.alarm.AlarmItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class UpsertAlarmUseCase(
    configuration: Configuration,
    private val alarmRepository: AlarmRepository
): UseCase<UpsertAlarmUseCase.Request, UpsertAlarmUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> {
        val alarmItem = request.alarmItem
        alarmRepository.upsertAlarm(alarmItem)
        return flowOf(Response)
    }

    data class Request(val alarmItem: AlarmItem): UseCase.Request

    data object Response: UseCase.Response
}