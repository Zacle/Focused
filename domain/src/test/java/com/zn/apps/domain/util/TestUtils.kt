package com.zn.apps.domain.util

import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.model.data.task.Pomodoro
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.data.task.TaskPriority
import com.zn.apps.model.data.task.TaskResource
import java.time.OffsetDateTime

object TestUtils {

    val today = OffsetDateTime.now()
    val yesterday = today.minusDays(1)
    val beforeYesterday = yesterday.minusDays(1)
    val anyDayInThePast = OffsetDateTime.now().withYear(2022)
    val tomorrow = today.plusDays(1)
    val afterTomorrow = today.plusDays(2)
    val anyDayInTheFuture = OffsetDateTime.now().withYear(2024)

    val sportTag = Tag(name = "sport")
    val workTag = Tag(name = "work")
    val studyTag = Tag(name = "study")

    val project_1 = Project(name = "UI design", color = 0)
    val project_2 = Project(name = "Machine Learning", color = 1)

    /**
     * Tasks with no project or tag and its task resources
     */
    /* Default task */
    val task_1_1 = Task(
        name = "task_1_1",
        tagId = sportTag.id
    )
    val taskResource_1_1 = TaskResource(
        task = task_1_1,
        tagId = sportTag.id,
        tagName = sportTag.getDisplayName()
    )
    /* due date today */
    val task_1_2 = Task(
        name = "task_1_2",
        dueDate = today,
        tagId = workTag.id,
        pomodoro = Pomodoro(
            pomodoroNumber = 5,
            pomodoroLength = 1800000
        )
    )
    val taskResource_1_2 = TaskResource(
        task = task_1_2,
        tagId = workTag.id,
        tagName = workTag.getDisplayName(),
        projectCompleted = true
    )
    /* priority medium */
    val task_1_3 = Task(
        name = "task_1_3",
        dueDate = tomorrow,
        priority = TaskPriority.MEDIUM,
        tagId = studyTag.id,
        pomodoro = Pomodoro(
            pomodoroNumber = 5,
            pomodoroLength = 1200000,
            pomodoroCompleted = 2,
            elapsedTime = 500000
        )
    )
    val taskResource_1_3 = TaskResource(
        task = task_1_3,
        tagId = studyTag.id,
        tagName = studyTag.getDisplayName()
    )
    val task_1_4 = Task(
        name = "task_1_4",
        priority = TaskPriority.HIGH
    )
    val taskResource_1_4 = TaskResource(
        task = task_1_4
    )
    /* due date yesterday */
    val task_2_1 = Task(
        name = "task_2_1",
        dueDate = yesterday,
        priority = TaskPriority.LOW,
        pomodoro = Pomodoro(
            pomodoroNumber = 3,
            pomodoroCompleted = 2,
            pomodoroLength = 7000000,
            elapsedTime = 14000000
        )
    )
    val taskResource_2_1 = TaskResource(
        task = task_2_1
    )
    val task_2_2 = Task(
        name = "task_2_2",
        dueDate = anyDayInTheFuture,
        tagId = workTag.id
    )
    val taskResource_2_2 = TaskResource(
        task = task_2_2,
        tagId = workTag.id,
        tagName = workTag.getDisplayName()
    )
    val task_2_3 = Task(
        name = "task_2_3",
        tagId = sportTag.id
    )
    val taskResource_2_3 = TaskResource(
        task = task_2_3,
        tagId = sportTag.id,
        tagName = sportTag.getDisplayName()
    )
    val task_2_4 = Task(
        name = "task_2_4",
        tagId = studyTag.id,
        dueDate = afterTomorrow
    )
    val taskResource_2_4 = TaskResource(
        task = task_2_4,
        tagId = studyTag.id,
        tagName = studyTag.getDisplayName()
    )
    val task_2_5 = Task(
        name = "task_2_5",
        tagId = studyTag.id,
        dueDate = anyDayInTheFuture
    )
    val taskResource_2_5 = TaskResource(
        task = task_2_5,
        tagId = studyTag.id,
        tagName = studyTag.getDisplayName()
    )
}