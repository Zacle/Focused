package com.zn.apps.ui_common.related_tasks

import android.content.Context
import com.zn.apps.domain.project.GetProjectTasksWithMetadata
import com.zn.apps.model.R
import com.zn.apps.model.usecase.UseCaseException
import com.zn.apps.ui_common.state.CommonResultConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RelatedProjectTasksUiConverter @Inject constructor(
    @ApplicationContext private val context: Context
): CommonResultConverter<GetProjectTasksWithMetadata.Response, RelatedTasksUiModel>() {

    override fun convertSuccess(data: GetProjectTasksWithMetadata.Response): RelatedTasksUiModel =
        RelatedTasksUiModel(
            screenTitle = data.project.name,
            metadata = data.metadataResult,
            relatedTasksGrouped = data.relatedTasksGrouped
        )

    override fun convertError(useCaseException: UseCaseException): String =
        when(useCaseException) {
            is UseCaseException.ProjectNotFoundException -> context.getString(R.string.project_not_found)
            else -> context.getString(R.string.unknown_error)
        }
}