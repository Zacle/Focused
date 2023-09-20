package com.zn.apps.feature.tasks.single

import android.content.Context
import com.zn.apps.domain.task.GetTaskUseCase
import com.zn.apps.model.R
import com.zn.apps.model.usecase.UseCaseException
import com.zn.apps.ui_common.state.CommonResultConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TaskUiConverter @Inject constructor(
    @ApplicationContext private val context: Context
): CommonResultConverter<GetTaskUseCase.Response, TaskUiModel>() {

    override fun convertSuccess(data: GetTaskUseCase.Response): TaskUiModel =
        TaskUiModel(task = data.taskResource.task)

    override fun convertError(useCaseException: UseCaseException): String =
        when(useCaseException) {
            is UseCaseException.TaskNotFoundException -> context.getString(R.string.task_not_found)
            else -> context.getString(R.string.unknown_error)
        }
}