package com.zn.apps.feature.tasks.list

import android.content.Context
import com.zn.apps.domain.GetTasksWithTagsUseCase
import com.zn.apps.model.R
import com.zn.apps.model.usecase.UseCaseException
import com.zn.apps.ui_common.state.CommonResultConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TasksUiConverter @Inject constructor(
    @ApplicationContext private val context: Context
): CommonResultConverter<GetTasksWithTagsUseCase.Response, TasksUiModel>() {

    override fun convertSuccess(data: GetTasksWithTagsUseCase.Response): TasksUiModel =
        TasksUiModel(
            groupedTasks = data.relatedTasksGrouped,
            tags = data.tags
        )

    override fun convertError(useCaseException: UseCaseException): String =
        when(useCaseException) {
            is UseCaseException.TaskNotFoundException -> context.getString(R.string.task_not_found)
            is UseCaseException.TaskNotUpdatedException -> context.getString(R.string.task_not_updated)
            else -> context.getString(R.string.unknown_error)
        }
}