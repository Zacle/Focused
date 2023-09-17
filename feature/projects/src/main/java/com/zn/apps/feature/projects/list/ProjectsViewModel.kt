package com.zn.apps.feature.projects.list

import androidx.lifecycle.viewModelScope
import com.zn.apps.domain.project.GetProjectResourcesUseCase
import com.zn.apps.model.data.project.ProjectFiltration
import com.zn.apps.ui_common.delegate.ProjectsViewModelDelegate
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val getProjectResourcesUseCase: GetProjectResourcesUseCase,
    projectsViewModelDelegate: ProjectsViewModelDelegate,
    private val converter: ProjectsUiConverter
): BaseViewModel<ProjectsUiModel, UiState<ProjectsUiModel>, ProjectsUiAction, ProjectsUiEvent>(),
    ProjectsViewModelDelegate by projectsViewModelDelegate {

    /**
     * Hold the current state for filtering projects
     */
    var projectFiltration = MutableStateFlow(ProjectFiltration())
        private set

    override fun initState(): UiState<ProjectsUiModel> = UiState.Loading

    init {
        submitAction(ProjectsUiAction.Load)
    }

    override fun handleAction(action: ProjectsUiAction) {
        when(action) {
            ProjectsUiAction.Load -> loadProjects()
            is ProjectsUiAction.SetProjectFilterType -> {
                projectFiltration.update { it.copy(filterType = action.filterType) }
            }
            is ProjectsUiAction.UpdateProjectQuery -> {
                projectFiltration.update { it.copy(query = it.query) }
            }
            ProjectsUiAction.SearchProjectPressed -> searchProjectPressed()
            ProjectsUiAction.SearchProjectDismissed -> searchProjectDismissed()
            ProjectsUiAction.DeleteProjectPressed -> deleteProjectPressed()
            ProjectsUiAction.DeleteProjectDismissed -> deleteProjectDismissed()
            ProjectsUiAction.DeleteProjectConfirmed -> { submitSingleEvent(ProjectsUiEvent.ProjectDeleted) }
            ProjectsUiAction.CompleteProjectPressed -> setProjectCompletedPressed()
            ProjectsUiAction.CompleteProjectDismissed -> setProjectCompletedDismissed()
            ProjectsUiAction.CompleteProjectConfirmed -> setProjectCompletedConfirmed()
            is ProjectsUiAction.NavigateToEditProject -> {
                submitSingleEvent(ProjectsUiEvent.NavigateToEditProject(action.projectId))
            }
            is ProjectsUiAction.NavigateToRelatedTasks -> {
                submitSingleEvent(ProjectsUiEvent.NavigateToRelatedTasks(action.projectId))
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadProjects() {
        viewModelScope.launch {
            projectFiltration
                .flatMapLatest { projectFiltration ->
                    getProjectResourcesUseCase.execute(
                        GetProjectResourcesUseCase.Request(projectFiltration)
                    )
                }
                .collectLatest { result ->
                    submitState(converter.convert(result))
                }
        }
    }
}