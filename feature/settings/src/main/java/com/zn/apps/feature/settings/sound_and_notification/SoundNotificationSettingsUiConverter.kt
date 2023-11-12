package com.zn.apps.feature.settings.sound_and_notification

import android.content.Context
import com.zn.apps.domain.datastore.GetReminderPreferencesUseCase
import com.zn.apps.model.R
import com.zn.apps.model.usecase.UseCaseException
import com.zn.apps.ui_common.state.CommonResultConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SoundNotificationSettingsUiConverter @Inject constructor(
    @ApplicationContext private val context: Context
): CommonResultConverter<GetReminderPreferencesUseCase.Response, SoundNotificationSettingsUiModel>() {

    override fun convertSuccess(data: GetReminderPreferencesUseCase.Response): SoundNotificationSettingsUiModel =
        SoundNotificationSettingsUiModel(
            reminderPreferences = data.reminderPreferences
        )

    override fun convertError(useCaseException: UseCaseException): String =
        context.getString(R.string.unknown_error)
}