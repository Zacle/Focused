package com.zn.apps.domain.datastore

import com.zn.apps.domain.UseCase
import com.zn.apps.domain.repository.ReminderPreferencesRepository
import com.zn.apps.model.datastore.ReminderPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetReminderPreferencesUseCase(
    configuration: Configuration,
    private val reminderPreferencesRepository: ReminderPreferencesRepository
): UseCase<GetReminderPreferencesUseCase.Request, GetReminderPreferencesUseCase.Response>(configuration) {

    override suspend fun process(request: Request): Flow<Response> =
        reminderPreferencesRepository.reminderPreferences.map { Response(it) }

    data object Request: UseCase.Request

    data class Response(val reminderPreferences: ReminderPreferences): UseCase.Response
}