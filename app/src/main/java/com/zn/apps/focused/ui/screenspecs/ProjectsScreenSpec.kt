package com.zn.apps.focused.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.zn.apps.feature.projects.list.ProjectsRoute
import com.zn.apps.feature.projects.list.ProjectsUiAction
import com.zn.apps.feature.projects.list.ProjectsUiEvent
import com.zn.apps.feature.projects.list.ProjectsViewModel
import com.zn.apps.focused.R
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_common.state.CommonScreen
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import com.zn.apps.ui_design.icon.Icon.DrawableResourceIcon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.zn.apps.feature.projects.R as projectsR

data object ProjectsScreenSpec: BottomNavScreenSpec {

    override val icon: Icon = DrawableResourceIcon(FAIcons.ProjectDestination)

    override val label: Int = projectsR.string.projects

    override val route: String = "projects"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        val viewModel: ProjectsViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.submitAction(ProjectsUiAction.Load)
        }

        val projectFiltration by viewModel.projectFiltration.collectAsStateWithLifecycle()
        val uiStateHolder by viewModel.projectsUiStateHolder.collectAsStateWithLifecycle()

        viewModel.uiStateFlow.collectAsStateWithLifecycle().value.let { state ->
            CommonScreen(
                state = state,
                tryAgain = { viewModel.submitAction(ProjectsUiAction.Load) }
            ) { projectsUiModel ->
                ProjectsRoute(
                    projectsUiModel = projectsUiModel,
                    projectFiltration = projectFiltration,
                    uiStateHolder = uiStateHolder,
                    coroutineScope = appState.coroutineScope,
                    onProjectPressed = { projectId ->
                        viewModel.submitAction(ProjectsUiAction.NavigateToRelatedTasks(projectId))
                    },
                    navigateToEditProject = { projectId ->
                        viewModel.submitAction(ProjectsUiAction.NavigateToEditProject(projectId))
                    },
                    upsertProject = {
                        viewModel.upsertProject(it)
                    },
                    onProjectFilterTypeSelected = {
                        viewModel.submitAction(ProjectsUiAction.SetProjectFilterType(it))
                    }
                )
            }
        }

        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.singleEventFlow.collectLatest {
                when(it) {
                    ProjectsUiEvent.ProjectDeleted -> {
                        showSnackbar(appState, context.getString(R.string.project_deleted))
                    }
                    is ProjectsUiEvent.NavigateToEditProject -> {
                        // TODO navigate to edit project
                    }
                    is ProjectsUiEvent.NavigateToRelatedTasks -> {
                        // TODO navigate to related tasks
                    }
                }
            }
        }
    }

    private suspend fun showSnackbar(appState: FocusedAppState, message: String) {
        appState.coroutineScope.launch {
            appState.snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = true
            )
        }
    }
}