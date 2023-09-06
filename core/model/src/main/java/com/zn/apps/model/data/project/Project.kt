package com.zn.apps.model.data.project

import java.time.OffsetDateTime
import java.util.UUID

data class Project(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var color: Int,
    var createdAt: OffsetDateTime = OffsetDateTime.now(),
    var tagId: String? = null,
    var completedTime: OffsetDateTime? = null,
    var completed: Boolean = false
)