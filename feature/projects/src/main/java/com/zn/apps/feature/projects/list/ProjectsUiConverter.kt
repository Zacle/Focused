package com.zn.apps.feature.projects.list

import android.content.Context
import com.zn.apps.domain.project.GetProjectResourcesUseCase
import com.zn.apps.feature.projects.R
import com.zn.apps.model.usecase.UseCaseException
import com.zn.apps.ui_common.state.CommonResultConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProjectsUiConverter @Inject constructor(
    @ApplicationContext private val context: Context
): CommonResultConverter<GetProjectResourcesUseCase.Response, ProjectsUiModel>() {

    override fun convertSuccess(data: GetProjectResourcesUseCase.Response): ProjectsUiModel =
        ProjectsUiModel(
            projectResources = data.projectResources
        )

    override fun convertError(useCaseException: UseCaseException) = context.getString(R.string.unknown_error)
}