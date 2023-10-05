package com.zn.apps.model.data.report

import java.time.OffsetDateTime

data class Report(
    val completedTime: OffsetDateTime,
    val elapsedTime: Long,
    val taskId: String? = null,
    val tagId: String? = null,
    val projectId: String? = null
)
