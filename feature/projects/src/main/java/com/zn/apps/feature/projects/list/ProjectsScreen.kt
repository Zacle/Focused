package com.zn.apps.feature.projects.list

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zn.apps.feature.projects.R
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.project.ProjectFilterType
import com.zn.apps.model.data.project.ProjectFiltration
import com.zn.apps.model.data.project.ProjectResource
import com.zn.apps.ui_common.delegate.ProjectsUiStateHolder
import com.zn.apps.ui_design.component.EmptyScreen
import com.zn.apps.ui_design.component.FAFloatingButton
import com.zn.apps.ui_design.component.FATopAppBar
import com.zn.apps.ui_design.component.FATopAppBarDefaults
import com.zn.apps.ui_design.icon.FAIcons
import com.zn.apps.ui_design.icon.Icon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsRoute(
    projectsUiModel: ProjectsUiModel,
    projectFiltration: ProjectFiltration,
    uiStateHolder: ProjectsUiStateHolder,
    coroutineScope: CoroutineScope,
    onProjectFilterTypeSelected: (ProjectFilterType) -> Unit,
    upsertProject: (Project) -> Unit,
    onProjectPressed: (String) -> Unit,
    navigateToEditProject: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val bottomSheetState = rememberModalBottomSheetState()
    var showModalBottomSheet by remember {
        mutableStateOf(false)
    }

    BackHandler(bottomSheetState.isVisible) {
        coroutineScope.launch {
            bottomSheetState.hide()
        }.invokeOnCompletion {
            if (!bottomSheetState.isVisible) {
                showModalBottomSheet = false
            }
        }
    }

    AnimatedVisibility(
        visible = showModalBottomSheet,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 3000, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically()
    ) {
        ModalBottomSheet(
            onDismissRequest = { showModalBottomSheet = false },
            sheetState = bottomSheetState,
            windowInsets = WindowInsets.ime.add(BottomSheetDefaults.windowInsets)
        ) {
            InsertProjectBottomSheetContent(
                upsertProject = upsertProject,
                tags = uiStateHolder.tags,
                shouldShowModalSheet = { showModalBottomSheet = it }
            )
        }
    }

    ProjectsScreen(
        projectsUiModel = projectsUiModel,
        projectFiltration = projectFiltration,
        onProjectFilterTypeSelected = onProjectFilterTypeSelected,
        onProjectPressed = onProjectPressed,
        navigateToEditProject = navigateToEditProject,
        shouldShowModalSheet = { showModalBottomSheet = it },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsScreen(
    projectsUiModel: ProjectsUiModel,
    projectFiltration: ProjectFiltration,
    onProjectFilterTypeSelected: (ProjectFilterType) -> Unit,
    onProjectPressed: (String) -> Unit,
    navigateToEditProject: (String) -> Unit,
    shouldShowModalSheet: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FATopAppBar(
                titleName = stringResource(id = R.string.projects),
                navigationIcon = Icon.DrawableResourceIcon(FAIcons.menu),
                onNavigationIconClicked = {},
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = FAIcons.search),
                            contentDescription = stringResource(id = R.string.search_project),
                            tint = FATopAppBarDefaults.appBarContentColor()
                        )
                    }
                }
            ) {
                ProjectsAppBarFilter(
                    onFilterSelected = onProjectFilterTypeSelected,
                    selected = projectFiltration.filterType
                )
            }
        },
        floatingActionButton = {
            FAFloatingButton(
                onClick = { shouldShowModalSheet(true) },
                painter = painterResource(id = R.drawable.add_project),
                description = stringResource(id = R.string.click_to_add_project)
            )
        },
        modifier = modifier
    ) { innerPadding ->

        Crossfade(
            targetState = projectFiltration,
            label = "projects screen crossfade",
            animationSpec = tween(durationMillis = 3000, easing = LinearOutSlowInEasing)
        ) { projectFiltration ->
            ProjectsContent(
                projectsUiModel = projectsUiModel,
                projectFiltration = projectFiltration,
                innerPadding = innerPadding,
                onProjectPressed = onProjectPressed,
                navigateToEditProject = navigateToEditProject
            )
        }
    }
}

@Composable
fun ProjectsContent(
    projectsUiModel: ProjectsUiModel,
    projectFiltration: ProjectFiltration,
    innerPadding: PaddingValues,
    onProjectPressed: (String) -> Unit,
    navigateToEditProject: (String) -> Unit
) {
    if (projectsUiModel.projectResources.isEmpty()) {
        if (projectFiltration.filterType == ProjectFilterType.COMPLETED) {
            EmptyScreen(
                message = stringResource(id = R.string.no_completed_project),
                description = "",
                resId = R.drawable.no_projects
            )
        } else {
            EmptyScreen(
                message = stringResource(id = R.string.no_project),
                description = stringResource(id = R.string.click_to_add_project),
                resId = R.drawable.no_projects
            )
        }
    } else {
        ProjectList(
            projectsUiModel = projectsUiModel,
            onProjectPressed = onProjectPressed,
            navigateToEditProject = navigateToEditProject,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun ProjectList(
    projectsUiModel: ProjectsUiModel,
    onProjectPressed: (String) -> Unit,
    navigateToEditProject: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        contentPadding = contentPadding
    ) {

        items(projectsUiModel.projectResources, key = { it.project.id }) { projectResource ->
            ProjectSection(
                projectResource = projectResource,
                onProjectPressed = onProjectPressed,
                navigateToEditProject = navigateToEditProject,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun ProjectSection(
    projectResource: ProjectResource,
    onProjectPressed: (String) -> Unit,
    navigateToEditProject: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ProjectsViewModel = hiltViewModel()
    val uiStateHolder by viewModel.projectsUiStateHolder.collectAsStateWithLifecycle()

    DraggableProjectListCard(
        projectResource = projectResource,
        isProjectRevealed = uiStateHolder.projectPressed?.id == projectResource.project.id,
        showCompleteProjectDialog = uiStateHolder.showCompleteProjectDialog,
        showDeleteProjectDialog = uiStateHolder.showDeleteProjectDialog,
        onCompletePressed = { viewModel.submitAction(ProjectsUiAction.CompleteProjectPressed) },
        onCompleteDismissed = { viewModel.submitAction(ProjectsUiAction.CompleteProjectDismissed) },
        onCompleteConfirmed = { viewModel.submitAction(ProjectsUiAction.CompleteProjectConfirmed) },
        onDeletePressed = { viewModel.submitAction(ProjectsUiAction.DeleteProjectPressed) },
        onDeleteDismissed = { viewModel.submitAction(ProjectsUiAction.DeleteProjectDismissed) },
        onDeleteConfirmed = { viewModel.submitAction(ProjectsUiAction.DeleteProjectConfirmed) },
        navigateToProject = onProjectPressed,
        navigateToEditProject = navigateToEditProject,
        onExpand = { viewModel.expand(projectResource.project) },
        onCollapse = { viewModel.collapse() },
        modifier = modifier
    )
}
