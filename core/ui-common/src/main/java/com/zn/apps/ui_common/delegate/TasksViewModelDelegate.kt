package com.zn.apps.ui_common.delegate

import com.zn.apps.common.alarm.TaskAlarmScheduler
import com.zn.apps.common.network.di.ApplicationScope
import com.zn.apps.domain.alarm.DeleteAlarmUseCase
import com.zn.apps.domain.alarm.UpsertAlarmUseCase
import com.zn.apps.domain.project.GetProjectsUseCase
import com.zn.apps.domain.tag.GetTagsUseCase
import com.zn.apps.domain.task.DeleteTaskUseCase
import com.zn.apps.domain.task.UpsertTaskUseCase
import com.zn.apps.model.data.alarm.AlarmItem
import com.zn.apps.model.data.task.Pomodoro
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.usecase.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.OffsetDateTime
import javax.inject.Inject

interface TasksViewModelDelegate {

    val tasksUiStateHolder: StateFlow<TasksUiStateHolder>

    /** set the task completed */
    fun setTaskCompleted(task: Task)

    /** flag the task that we intend to update the due date */
    fun updateDueDatePressed()

    /** Ignore the task that has been flagged */
    fun updateDueDateDismissed()

    /**
     * Update the due date of the task that was flagged
     *
     * @return [Result] if the update was successful or unsuccessful to notify the user
     */
    fun updateDueDateConfirmed(
        offsetDateTime: OffsetDateTime?,
        remindTaskAt: Int,
        isReminderSet: Boolean
    )

    /** flag the task that we intend to update the pomodoro */
    fun updatePomodoroPressed()

    /** Ignore the task that has been flagged */
    fun updatePomodoroDismissed()

    /**
     * Update the pomodoro of the task that was flagged
     *
     * @return [Result] if the update was successful or unsuccessful to notify the user
     */
    fun updatePomodoroConfirmed(pomodoro: Pomodoro)

    /** flag the task that we intend to update to delete */
    fun deleteTaskPressed()

    /** Ignore the task that has been flagged */
    fun deleteTaskDismissed()

    /**
     * Delete the task that was flagged
     *
     * @return [Result] if the delete was successful or unsuccessful to notify the user
     */
    fun deleteTaskConfirmed()

    /**
     * Insert or modify the task
     */
    fun upsertTask(task: Task)

    /**
     * Record the task expand to show options
     */
    fun expand(task: Task)

    /**
     * Remove the recorded task
     */
    fun collapse()
}

class DefaultTasksViewModelDelegate @Inject constructor(
    private val upsertTaskUseCase: UpsertTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getTagsUseCase: GetTagsUseCase,
    private val getProjectsUseCase: GetProjectsUseCase,
    private val upsertAlarmUseCase: UpsertAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
    private val taskAlarmScheduler: TaskAlarmScheduler,
    @ApplicationScope val scope: CoroutineScope
): TasksViewModelDelegate {

    /**
     * State Holder for screen dialogs
     */
    override val tasksUiStateHolder = MutableStateFlow(TasksUiStateHolder())

    init {
        scope.launch {
            combine(
                getProjectsUseCase.execute(GetProjectsUseCase.Request),
                getTagsUseCase.execute(GetTagsUseCase.Request)
            ) { projectsResult, tagsResult ->
                Pair(projectsResult, tagsResult)
            }.collectLatest { pair ->
                if (pair.first is Result.Success) {
                    tasksUiStateHolder.update {
                        it.copy(
                            projects = (pair.first as Result.Success<GetProjectsUseCase.Response>).data.projects
                        )
                    }
                }
                if (pair.second is Result.Success) {
                    tasksUiStateHolder.update {
                        it.copy(
                            tags = (pair.second as Result.Success<GetTagsUseCase.Response>).data.tags
                        )
                    }
                }
            }
        }
    }

    private fun upsert(task: Task) {
        scope.launch {
            upsertTaskUseCase.execute(
                UpsertTaskUseCase.Request(
                    task = task
                )
            )
        }
    }

    override fun setTaskCompleted(task: Task) {
        upsert(task.copy(completed = true, completedTime = OffsetDateTime.now()))
    }

    override fun updateDueDatePressed() {
        tasksUiStateHolder.update {
            it.copy(showDueDateDialog = true)
        }
    }

    override fun updateDueDateDismissed() {
        tasksUiStateHolder.update {
            it.copy(showDueDateDialog = false, taskPressed = null)
        }
    }

    override fun updateDueDateConfirmed(
        offsetDateTime: OffsetDateTime?,
        remindTaskAt: Int,
        isReminderSet: Boolean
    ) {
        Timber.d("reminder set = $isReminderSet")
        val task = tasksUiStateHolder.value.taskPressed
        if (task != null) {
            upsert(
                task.copy(
                    dueDate = offsetDateTime,
                    remindTaskAt = remindTaskAt,
                    shouldRemindTask = isReminderSet
                )
            )
            scope.launch {
                if (isReminderSet && offsetDateTime != null) {
                    upsertAlarm(
                        AlarmItem(
                            task = task,
                            remindAt = offsetDateTime.minusMinutes(remindTaskAt.toLong())
                        )
                    )
                }
                if (!isReminderSet) {
                    cancelAlarm(
                        AlarmItem(
                            task = task,
                            remindAt = OffsetDateTime.now()
                        )
                    )
                }
            }
        }
        tasksUiStateHolder.update {
            it.copy(showDueDateDialog = false, taskPressed = null)
        }
    }

    override fun updatePomodoroPressed() {
        tasksUiStateHolder.update {
            it.copy(showPomodoroDialog = true)
        }
    }

    override fun updatePomodoroDismissed() {
        tasksUiStateHolder.update {
            it.copy(showPomodoroDialog = false, taskPressed = null)
        }
    }

    override fun updatePomodoroConfirmed(pomodoro: Pomodoro) {
        val task = tasksUiStateHolder.value.taskPressed
        if (task != null) {
            upsert(task.copy(pomodoro = pomodoro))
        }
        tasksUiStateHolder.update {
            it.copy(showPomodoroDialog = false, taskPressed = null)
        }
    }

    override fun deleteTaskPressed() {
        tasksUiStateHolder.update {
            it.copy(showDeleteTaskDialog = true)
        }
    }

    override fun deleteTaskDismissed() {
        tasksUiStateHolder.update {
            it.copy(showDeleteTaskDialog = false, taskPressed = null)
        }
    }

    override fun deleteTaskConfirmed() {
        val task = tasksUiStateHolder.value.taskPressed
        if (task != null) {
            scope.launch {
                deleteTaskUseCase.execute(
                    DeleteTaskUseCase.Request(
                        task = task
                    )
                )
                cancelAlarm(
                    AlarmItem(
                        task = task,
                        remindAt = OffsetDateTime.now()
                    )
                )
            }
        }
        tasksUiStateHolder.update {
            it.copy(showDeleteTaskDialog = false, taskPressed = null)
        }
    }

    private fun cancelAlarm(alarmItem: AlarmItem) {
        scope.launch {
            taskAlarmScheduler.cancelAlarm(alarmItem)
            deleteAlarmUseCase.execute(
                DeleteAlarmUseCase.Request(alarmItem.task.id)
            )
        }
    }

    private fun upsertAlarm(alarmItem: AlarmItem) {
        scope.launch {
            taskAlarmScheduler.scheduleAlarm(alarmItem)
            upsertAlarmUseCase.execute(
                UpsertAlarmUseCase.Request(alarmItem)
            )
        }
    }

    override fun upsertTask(task: Task) {
        upsert(task)
        if (task.shouldRemindTask && task.dueDate != null) {
            upsertAlarm(
                AlarmItem(
                    task = task,
                    remindAt = task.dueDate!!.minusMinutes(task.remindTaskAt.toLong())
                )
            )
        }
    }

    override fun expand(task: Task) {
        tasksUiStateHolder.update {
            it.copy(taskPressed = task)
        }
    }

    override fun collapse() {
        tasksUiStateHolder.update {
            it.copy(taskPressed = null)
        }
    }
}