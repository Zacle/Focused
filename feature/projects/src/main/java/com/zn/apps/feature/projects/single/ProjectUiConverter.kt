package com.zn.apps.feature.projects.single

import android.content.Context
import com.zn.apps.domain.project.GetProjectUseCase
import com.zn.apps.model.R
import com.zn.apps.model.usecase.UseCaseException
import com.zn.apps.ui_common.state.CommonResultConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProjectUiConverter @Inject constructor(
    @ApplicationContext private val context: Context
): CommonResultConverter<GetProjectUseCase.Response, ProjectUiModel>() {

    override fun convertSuccess(data: GetProjectUseCase.Response): ProjectUiModel =
        ProjectUiModel(project = data.project)

    override fun convertError(useCaseException: UseCaseException): String {
        return when(useCaseException) {
            is UseCaseException.ProjectNotFoundException -> context.getString(R.string.project_not_found)
            else -> context.getString(R.string.unknown_error)
        }
    }
}