package com.zn.apps.data_local.model

import com.zn.apps.data_local.database.report.ReportEntity
import com.zn.apps.model.data.report.Report

fun ReportEntity.asExternalModel() =
    Report(completedTime, elapsedTime, taskId, tagId, projectId)

fun Report.asEntity() =
    ReportEntity(completedTime, elapsedTime, taskId, tagId, projectId)