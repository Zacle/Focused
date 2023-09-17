package com.zn.apps.ui_common.delegate

import com.zn.apps.common.network.di.ApplicationScope
import com.zn.apps.domain.project.DeleteProjectUseCase
import com.zn.apps.domain.project.UpsertProjectUseCase
import com.zn.apps.domain.tag.GetTagsUseCase
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.usecase.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject

interface ProjectsViewModelDelegate {

    val projectsUiStateHolder: StateFlow<ProjectsUiStateHolder>

    /** flag the project that we intend to complete */
    fun setProjectCompletedPressed()

    /** Ignore the project that has been flagged */
    fun setProjectCompletedDismissed()

    /**
     * set the project that was flagged as completed
     *
     * @return [Result] if the update was successful or unsuccessful to notify the user
     */
    fun setProjectCompletedConfirmed()

    /** flag the project that we intend to update to delete */
    fun deleteProjectPressed()

    /** Ignore the project that has been flagged */
    fun deleteProjectDismissed()

    /**
     * Delete the project that was flagged
     *
     * @return [Result] if the delete was successful or unsuccessful to notify the user
     */
    fun deleteProjectConfirmed(): Result<Any>?

    /**
     * mark that the user is currently searching
     */
    fun searchProjectPressed()

    /**
     * the user has stopped searching
     */
    fun searchProjectDismissed()

    /**
     * Add or modify the project
     */
    fun upsertProject(project: Project)

    /**
     * flag the project card that was expanded
     */
    fun expand(project: Project)

    /**
     * The project card that was flagged has been collapsed
     */
    fun collapse()
}

class DefaultProjectsViewModelDelegate @Inject constructor(
    private val upsertProjectUseCase: UpsertProjectUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    getTagsUseCase: GetTagsUseCase,
    @ApplicationScope val scope: CoroutineScope
): ProjectsViewModelDelegate {

    override val projectsUiStateHolder = MutableStateFlow(ProjectsUiStateHolder())

    init {
        scope.launch {
            getTagsUseCase.execute(GetTagsUseCase.Request)
                .collectLatest { result ->
                    if (result is Result.Success) {
                        projectsUiStateHolder.update {
                            it.copy(tags = result.data.tags)
                        }
                    }
                }
        }
    }

    override fun setProjectCompletedPressed() {
        projectsUiStateHolder.update {
            it.copy(showCompleteProjectDialog = true)
        }
    }

    override fun setProjectCompletedDismissed() {
        projectsUiStateHolder.update {
            it.copy(showCompleteProjectDialog = false, projectPressed = null)
        }
    }

    override fun setProjectCompletedConfirmed() {
        val project = projectsUiStateHolder.value.projectPressed
        if (project != null) {
            scope.launch {
                val request = UpsertProjectUseCase.Request(
                    project.copy(completed = true, completedTime = OffsetDateTime.now())
                )
                upsertProjectUseCase.execute(request)
            }
        }
        projectsUiStateHolder.update {
            it.copy(showCompleteProjectDialog = false, projectPressed = null)
        }
    }

    override fun deleteProjectPressed() {
        projectsUiStateHolder.update {
            it.copy(showDeleteProjectDialog = true)
        }
    }

    override fun deleteProjectDismissed() {
        projectsUiStateHolder.update {
            it.copy(showDeleteProjectDialog = false, projectPressed = null)
        }
    }

    override fun deleteProjectConfirmed(): Result<Any>? {
        val project = projectsUiStateHolder.value.projectPressed
        var result: Result<Any>? = null
        if (project != null) {
            scope.launch {
                deleteProjectUseCase.execute(
                    DeleteProjectUseCase.Request(project)
                ).collect {
                    result = it
                }
            }
        }
        projectsUiStateHolder.update {
            it.copy(showDeleteProjectDialog = false, projectPressed = null)
        }
        return result
    }

    override fun searchProjectPressed() {
        projectsUiStateHolder.update {
            it.copy(isSearching = true)
        }
    }

    override fun searchProjectDismissed() {
        projectsUiStateHolder.update {
            it.copy(isSearching = false)
        }
    }

    override fun upsertProject(project: Project) {
        scope.launch {
            upsertProjectUseCase.execute(
                UpsertProjectUseCase.Request(project)
            )
        }
    }

    override fun expand(project: Project) {
        projectsUiStateHolder.update {
            it.copy(projectPressed = project)
        }
    }

    override fun collapse() {
        projectsUiStateHolder.update {
            it.copy(projectPressed = null)
        }
    }
}