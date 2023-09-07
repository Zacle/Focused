package com.zn.apps.data_local.database.util

import android.graphics.Color
import com.zn.apps.data_local.database.project.PopulatedProjectEntity
import com.zn.apps.data_local.database.project.ProjectEntity
import com.zn.apps.data_local.database.tag.TagEntity
import com.zn.apps.data_local.database.task.PopulatedTaskEntity
import com.zn.apps.data_local.model.asEntity
import com.zn.apps.model.data.project.Project
import com.zn.apps.model.data.tag.Tag
import com.zn.apps.model.data.task.Pomodoro
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.data.task.TaskPriority
import java.time.OffsetDateTime

object TaskEntities {
    // default task
    val task_1 = Task(
        name = "task_1"
    )
    val taskEntity_1 = task_1.asEntity()
    val populatedTaskEntity_1 = PopulatedTaskEntity(
        task = taskEntity_1,
        tag = emptyList(),
        project = emptyList()
    )

    // pomodoro set
    val task_2 = Task(
        name = "task_2",
        pomodoro = Pomodoro(
            pomodoroNumber = 4,
            pomodoroLength = 50000
        ),
        dueDate = OffsetDateTime.now(),
        priority = TaskPriority.LOW
    )
    val taskEntity_2 = task_2.asEntity()
    val populatedTaskEntity_2 = PopulatedTaskEntity(
        task = taskEntity_2,
        tag = emptyList(),
        project = emptyList()
    )

    // project id set
    private val project = ProjectEntity(id = "projectId", name = "name", color = 1, createdAt = OffsetDateTime.now())
    val task_3 = Task(
        name = "task_3",
        dueDate = OffsetDateTime.now().plusDays(5),
        priority = TaskPriority.MEDIUM,
    )
    val taskEntity_3 = task_3.asEntity()
    val populatedTaskEntity_3 = PopulatedTaskEntity(
        task = taskEntity_3,
        tag = emptyList(),
        project = emptyList()
    )

    // tag id set
    val tag = TagEntity(id = "tagId", name = "name")
    private val task_4 = Task(
        name = "task_4",
        completed = true,
        completedTime = OffsetDateTime.now().minusDays(1)
    )
    val taskEntity_4 = task_4.asEntity()
    val populatedTaskEntity_4 = PopulatedTaskEntity(
        task = taskEntity_4,
        tag = emptyList(),
        project = emptyList()
    )

    // completed set
    val task_5 = Task(
        name = "task_5",
        completed = true,
        completedTime = OffsetDateTime.now().minusHours(4),
        dueDate = OffsetDateTime.now()
    )
    val taskEntity_5 = task_5.asEntity()
    val populatedTaskEntity_5 = PopulatedTaskEntity(
        task = taskEntity_5,
        tag = emptyList(),
        project = emptyList()
    )

    /**
     * Tags
     */
    val studyTag = Tag(name = "studyTag", color = Color.GREEN).asEntity()
    val workTag = Tag(name = "workTag", color = Color.YELLOW).asEntity()
    val sportTag = Tag(name = "sportTag", color = Color.LTGRAY).asEntity()
    val designTag = Tag(name = "designTag", color = Color.WHITE).asEntity()
    val otherTag = Tag(name = "otherTag", color = Color.BLUE).asEntity()

    /**
     * Projects
     */
    val project_1 = Project(name = "project_1", color = 1, tagId = studyTag.id)
    val project_2 = Project(name = "project_2", color = 2)
    val project_3 = Project(name = "project_3", color = 3)
    val project_4 = Project(name = "project_4", color = 1, tagId = designTag.id)

    val populatedProjectEntity_1 = PopulatedProjectEntity(
        project = project_1.asEntity(),
        tags = listOf(studyTag),
        tasks = listOf(task_1, task_2, task_4).map { it.asEntity() }
    )
    val populatedProjectEntity_2 = PopulatedProjectEntity(
        project = project_2.asEntity(),
        tags = emptyList(),
        tasks = emptyList()
    )
    val populatedProjectEntity_3 = PopulatedProjectEntity(
        project = project_3.asEntity(),
        tags = emptyList(),
        tasks = listOf(task_3).map { it.asEntity() }
    )
}