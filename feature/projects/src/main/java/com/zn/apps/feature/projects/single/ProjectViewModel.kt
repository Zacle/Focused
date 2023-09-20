package com.zn.apps.feature.projects.single

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.zn.apps.domain.project.GetProjectUseCase
import com.zn.apps.feature.projects.single.ProjectUiAction.DeleteProjectConfirmed
import com.zn.apps.feature.projects.single.ProjectUiAction.DeleteProjectDismissed
import com.zn.apps.feature.projects.single.ProjectUiAction.DeleteProjectPressed
import com.zn.apps.feature.projects.single.ProjectUiAction.Load
import com.zn.apps.feature.projects.single.ProjectUiAction.NavigateBackPressed
import com.zn.apps.feature.projects.single.ProjectUiAction.UpdatePressed
import com.zn.apps.ui_common.delegate.ProjectsViewModelDelegate
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PROJECT_ID_ARGUMENT = "projectId"

@HiltViewModel
class ProjectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProjectUseCase: GetProjectUseCase,
    private val projectsViewModelDelegate: ProjectsViewModelDelegate,
    private val converter: ProjectUiConverter
): BaseViewModel<ProjectUiModel, UiState<ProjectUiModel>, ProjectUiAction, ProjectUiEvent>(),
    ProjectsViewModelDelegate by projectsViewModelDelegate {

    /** id parameter passed to the route **/
    private val projectId: String = requireNotNull(savedStateHandle[PROJECT_ID_ARGUMENT])

    override fun initState(): UiState<ProjectUiModel> = UiState.Loading

    init {
        submitAction(Load)
    }

    override fun handleAction(action: ProjectUiAction) {
        when(action) {
            Load -> loadProject()
            is UpdatePressed -> {
                upsertProject(action.project)
                submitSingleEvent(ProjectUiEvent.ProjectUpdated)
            }
            DeleteProjectPressed -> deleteProjectPressed()
            DeleteProjectDismissed -> deleteProjectDismissed()
            is DeleteProjectConfirmed -> {
                expand(action.project)
                deleteProjectConfirmed()
                submitSingleEvent(ProjectUiEvent.NavigateBack)
            }
            NavigateBackPressed -> submitSingleEvent(ProjectUiEvent.NavigateBack)
        }
    }

    private fun loadProject() {
        viewModelScope.launch {
            getProjectUseCase.execute(
                GetProjectUseCase.Request(projectId)
            ).collectLatest { result ->
                submitState(converter.convert(result))
            }
        }
    }
}