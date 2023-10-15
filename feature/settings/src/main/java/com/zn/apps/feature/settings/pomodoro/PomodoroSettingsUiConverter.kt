package com.zn.apps.feature.settings.pomodoro

import android.content.Context
import com.zn.apps.domain.datastore.GetPomodoroPreferencesUseCase
import com.zn.apps.model.R
import com.zn.apps.model.usecase.UseCaseException
import com.zn.apps.ui_common.state.CommonResultConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PomodoroSettingsUiConverter @Inject constructor(
    @ApplicationContext private val context: Context
): CommonResultConverter<GetPomodoroPreferencesUseCase.Response, PomodoroSettingsUiModel>() {

    override fun convertSuccess(data: GetPomodoroPreferencesUseCase.Response): PomodoroSettingsUiModel =
        PomodoroSettingsUiModel(pomodoroPreferences = data.pomodoroPreferences)

    override fun convertError(useCaseException: UseCaseException): String =
        context.getString(R.string.unknown_error)
}