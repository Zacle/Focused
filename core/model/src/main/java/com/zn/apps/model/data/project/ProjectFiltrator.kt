package com.zn.apps.model.data.project

/**
 * Helper to handle all types of filter in the projects list screen
 *
 * @param filterType filter project based on the current status
 * @param query filter project that matches the query and also the [filterType]
 */
data class ProjectFiltrator(
    val filterType: ProjectFilterType,
    val query: String = ""
)
