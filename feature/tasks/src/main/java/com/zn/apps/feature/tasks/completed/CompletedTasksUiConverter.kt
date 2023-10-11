package com.zn.apps.feature.tasks.completed

import android.content.Context
import com.zn.apps.domain.GetTasksWithTagsUseCase
import com.zn.apps.model.R
import com.zn.apps.model.usecase.UseCaseException
import com.zn.apps.ui_common.state.CommonResultConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CompletedTasksUiConverter @Inject constructor(
    @ApplicationContext private val context: Context
): CommonResultConverter<GetTasksWithTagsUseCase.Response, CompletedTasksUiModel>() {

    override fun convertSuccess(data: GetTasksWithTagsUseCase.Response): CompletedTasksUiModel =
        CompletedTasksUiModel(
            groupedTasks = data.relatedTasksGrouped,
            tags = data.tags
        )

    override fun convertError(useCaseException: UseCaseException): String =
        context.getString(R.string.unknown_error)
}