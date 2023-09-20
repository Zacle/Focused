package com.zn.apps.focused.ui.screenspecs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zn.apps.feature.projects.R
import com.zn.apps.feature.projects.single.PROJECT_ID_ARGUMENT
import com.zn.apps.feature.projects.single.ProjectRoute
import com.zn.apps.feature.projects.single.ProjectUiAction
import com.zn.apps.feature.projects.single.ProjectUiEvent
import com.zn.apps.feature.projects.single.ProjectViewModel
import com.zn.apps.focused.ui.FocusedAppState
import com.zn.apps.ui_common.state.CommonScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data object ProjectScreenSpec: ScreenSpec {

    override val route: String = "projects/{${PROJECT_ID_ARGUMENT}}"

    override val arguments: List<NamedNavArgument>
        get() = listOf(
            navArgument(PROJECT_ID_ARGUMENT) { type = NavType.StringType }
        )

    fun buildRoute(projectId: String) = "projects/$projectId"

    @Composable
    override fun Content(appState: FocusedAppState, navBackStackEntry: NavBackStackEntry) {
        val viewModel: ProjectViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            viewModel.submitAction(ProjectUiAction.Load)
        }

        val uiStateHolder by viewModel.projectsUiStateHolder.collectAsStateWithLifecycle()

        viewModel.uiStateFlow.collectAsStateWithLifecycle().value.let { state ->
            CommonScreen(state = state) { projectUiModel ->
                ProjectRoute(
                    projectUiModel = projectUiModel,
                    uiStateHolder = uiStateHolder,
                    onUpdatePressed = {
                        viewModel.submitAction(ProjectUiAction.UpdatePressed(it))
                    },
                    onDeletePressed = {
                        viewModel.submitAction(ProjectUiAction.DeleteProjectPressed)
                    },
                    onDeleteDismissed = {
                        viewModel.submitAction(ProjectUiAction.DeleteProjectDismissed)
                    },
                    onDeleteConfirmed = {
                        viewModel.submitAction(ProjectUiAction.DeleteProjectConfirmed(it))
                    },
                    onUpPressed = {
                        viewModel.submitAction(ProjectUiAction.NavigateBackPressed)
                    }
                )
            }
        }
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            viewModel.singleEventFlow.collectLatest {
                when(it) {
                    ProjectUiEvent.ProjectUpdated -> {
                        showSnackbar(appState, context.getString(R.string.project_updated))
                    }
                    ProjectUiEvent.NavigateBack -> {
                        appState.navController.navigateUp()
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

fun NavGraphBuilder.projectScreen(appState: FocusedAppState) {
    composable(
        route = ProjectScreenSpec.route,
        arguments = ProjectScreenSpec.arguments,
        deepLinks = ProjectScreenSpec.deepLinks
    ) {
        ProjectScreenSpec.Content(appState = appState, navBackStackEntry = it)
    }
}