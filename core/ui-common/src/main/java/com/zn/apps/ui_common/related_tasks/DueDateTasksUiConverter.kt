package com.zn.apps.ui_common.related_tasks

import android.content.Context
import com.zn.apps.domain.task.GetTasksWithMetadataUseCase
import com.zn.apps.model.R
import com.zn.apps.model.usecase.UseCaseException
import com.zn.apps.ui_common.state.CommonResultConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DueDateTasksUiConverter @Inject constructor(
    @ApplicationContext private val context: Context
): CommonResultConverter<GetTasksWithMetadataUseCase.Response, RelatedTasksUiModel>() {

    override fun convertSuccess(data: GetTasksWithMetadataUseCase.Response): RelatedTasksUiModel =
        RelatedTasksUiModel(
            metadata = data.metadataResult,
            relatedTasksGrouped = data.relatedTasksGrouped
        )

    override fun convertError(useCaseException: UseCaseException): String =
        context.getString(R.string.unknown_error)
}