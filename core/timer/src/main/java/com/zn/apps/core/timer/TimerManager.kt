package com.zn.apps.core.timer

import com.zn.apps.common.minutesToMilliseconds
import com.zn.apps.common.network.di.ApplicationScope
import com.zn.apps.core.notification.TimerNotifier
import com.zn.apps.core.timer.service.TimerServiceManager
import com.zn.apps.domain.report.InsertReportUseCase
import com.zn.apps.domain.repository.PomodoroPreferencesRepository
import com.zn.apps.domain.repository.PomodoroStateRepository
import com.zn.apps.domain.task.GetTaskUseCase
import com.zn.apps.domain.task.UpsertTaskUseCase
import com.zn.apps.model.data.pomodoro.PomodoroPhase
import com.zn.apps.model.data.pomodoro.PomodoroState
import com.zn.apps.model.data.pomodoro.TimerState
import com.zn.apps.model.data.report.Report
import com.zn.apps.model.data.task.Task
import com.zn.apps.model.usecase.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimerManager @Inject constructor(
    private val countDownTimer: CountDownTimer,
    pomodoroPreferencesRepository: PomodoroPreferencesRepository,
    private val pomodoroStateRepository: PomodoroStateRepository,
    private val notifier: TimerNotifier,
    private val timerServiceManager: TimerServiceManager,
    private val getTaskUseCase: GetTaskUseCase,
    private val upsertTaskUseCase: UpsertTaskUseCase,
    private val insertReportUseCase: InsertReportUseCase,
    @ApplicationScope private val scope: CoroutineScope
) {
    private val pomodoroPreferences = pomodoroPreferencesRepository.pomodoroPreferences
    val pomodoroTimerState = pomodoroStateRepository.pomodoroState

    init {
        scope.launch {
            pomodoroStateRepository.apply {
                setTimerState(TimerState.IDLE)
                setTaskIdRunning("")
                setPomodoroCompletedSoFar(0)
                setCurrentPhase(PomodoroPhase.POMODORO)
            }
            pomodoroPreferences.collectLatest { preferences ->
                pomodoroStateRepository.setPomodoroToBeCompletedBeforeLongBreak(preferences.numberOfPomodoroBeforeLongBreak)
                val pomodoroState = pomodoroTimerState.first()
                val timerState = pomodoroState.timerState
                val taskIdRunning = pomodoroState.taskIdRunning
                val currentPhase = pomodoroState.currentPhase
                if (timerState == TimerState.IDLE && taskIdRunning.isEmpty()) {
                    val targetTimeInMillis =
                        preferences.lengthInMinutesForPhase(currentPhase).minutesToMilliseconds()
                    pomodoroStateRepository.setTargetTimeInMillis(targetTimeInMillis)
                    pomodoroStateRepository.setTimeLeftInMillis(targetTimeInMillis)
                }
                if (timerState == TimerState.RUNNING)
                    startTimer()
            }
        }
    }

    fun startTimer() {
        notifier.removeTimerCompletedNotification()
        scope.launch {
            countDownTimer.startTimer(
                durationInMillis = pomodoroTimerState.first().timeLeftInMillis,
                countDownInterval = 1_000L,
                onTick = { timeLeftInMillis ->
                    pomodoroStateRepository.setTimeLeftInMillis(timeLeftInMillis)
                },
                onFinished = {
                    onFinished()
                }
            )
            pomodoroStateRepository.setTimerState(TimerState.RUNNING)
            timerServiceManager.startService()
        }
    }

    fun pauseTimer() {
        scope.launch {
            pomodoroStateRepository.setTimerState(TimerState.PAUSED)
            countDownTimer.stopTimer()
        }
    }

    fun stopTimer() {
        scope.launch {
            pomodoroStateRepository.setTaskIdRunning("")
            resetPomodoroState(PomodoroPhase.POMODORO)
            countDownTimer.stopTimer()
            notifier.removeTimerServiceNotification()
            timerServiceManager.stopService()
        }
    }

    fun skipBreak() {
        scope.launch {
            val isBreak = pomodoroTimerState.first().currentPhase.isBreak
            if (isBreak) {
                countDownTimer.stopTimer()
                timerServiceManager.stopService()
                notifier.removeTimerServiceNotification()
                nextPhase()
            }
        }
    }

    fun updatePomodoroStateManagerAndStartTimer(task: Task) {
        scope.launch {
            pomodoroStateRepository.apply {
                setTaskIdRunning(task.id)
                setCurrentPhase(PomodoroPhase.POMODORO)
                setTimeLeftInMillis(task.pomodoro.pomodoroLength)
                setTargetTimeInMillis(task.pomodoro.pomodoroLength)
            }
            startTimer()
        }
    }

    /**
     * Reset the pomodoro state.
     *
     * @param phase the next phase following the reset
     */
    private suspend fun resetPomodoroState(phase: PomodoroPhase) {
        val pomodoroPreferenceInMillis = getPomodoroPhaseLengthInMillis(phase)
        pomodoroStateRepository.apply {
            setTimerState(TimerState.IDLE)
            setTimeLeftInMillis(pomodoroPreferenceInMillis)
            setTargetTimeInMillis(pomodoroPreferenceInMillis)
            setCurrentPhase(phase)
        }
    }

    private suspend fun getPomodoroPhaseLengthInMillis(phase: PomodoroPhase): Long {
        val pomodoroPreferences = pomodoroPreferences.first()
        return pomodoroPreferences.lengthInMinutesForPhase(phase).minutesToMilliseconds()
    }

    private suspend fun onFinished() {
        val pomodoroState = pomodoroTimerState.first()
        val currentPhase = pomodoroState.currentPhase
        if (currentPhase == PomodoroPhase.POMODORO) {
            val taskIdRunning = pomodoroState.taskIdRunning
            if (taskIdRunning.isNotEmpty()) {
                pomodoroStateRepository.setPomodoroCompletedSoFar(pomodoroState.pomodoroCompletedSoFar + 1)
                updateTask(taskIdRunning, pomodoroState.targetTimeInMillis)
            } else {
                val report = Report(
                    completedTime = OffsetDateTime.now(),
                    elapsedTime = pomodoroState.targetTimeInMillis
                )
                insertReportUseCase.execute(
                    InsertReportUseCase.Request(report)
                )
            }
        }
        notifier.removeTimerServiceNotification()
        notifier.showTimerCompletedNotification(currentPhase)
        timerServiceManager.stopService()
        nextPhase()
    }

    private suspend fun nextPhase() {
        val pomodoroState = pomodoroTimerState.first()
        val nextPhase = getNextPhase(pomodoroState.currentPhase, isLongBreakReached(pomodoroState))

        updatePomodoroStateForNextPhase(nextPhase, pomodoroState.taskIdRunning)
        notifier.removeTimerServiceNotification()
    }

    /**
     * Update the pomodoro state to hold values for the next state
     *
     * @param nextPhase upcoming phase to run with timer
     * @param taskIdRunning if the task is running, we only update the time otherwise we do a complete reset
     */
    private suspend fun updatePomodoroStateForNextPhase(
        nextPhase: PomodoroPhase,
        taskIdRunning: String
    ) {
        when (nextPhase) {
            PomodoroPhase.POMODORO -> {
                if (taskIdRunning.isNotEmpty()) {
                    pomodoroStateRepository.apply {
                        val queryTaskResponse: Result<GetTaskUseCase.Response> = getTaskUseCase.execute(
                            GetTaskUseCase.Request(taskIdRunning)
                        ).first()

                        if (queryTaskResponse is Result.Success) {
                            val task: Task = queryTaskResponse.data.taskResource.task
                            setCurrentPhase(nextPhase)
                            setTimerState(TimerState.IDLE)
                            setTimeLeftInMillis(task.pomodoro.pomodoroLength)
                            setTargetTimeInMillis(task.pomodoro.pomodoroLength)
                        } else {
                            resetPomodoroState(nextPhase)
                        }
                    }
                } else {
                    resetPomodoroState(nextPhase)
                }
            }
            PomodoroPhase.LONG_BREAK -> {
                pomodoroStateRepository.setPomodoroCompletedSoFar(0)
                resetPomodoroState(nextPhase)
            }
            else -> resetPomodoroState(nextPhase)
        }
    }

    private fun getNextPhase(phase: PomodoroPhase, longBreakReached: Boolean): PomodoroPhase {
        return when (phase) {
            PomodoroPhase.POMODORO -> if (longBreakReached) PomodoroPhase.LONG_BREAK else PomodoroPhase.SHORT_BREAK
            PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> PomodoroPhase.POMODORO
        }
    }

    private fun isLongBreakReached(pomodoroState: PomodoroState): Boolean {
        val pomodoroCompletedSoFar = pomodoroState.pomodoroCompletedSoFar
        val pomodoroBeforeLongBreak = pomodoroState.pomodoroToBeCompletedBeforeLongBreak
        return pomodoroCompletedSoFar == pomodoroBeforeLongBreak
    }

    /**
     * If the task is completed, update the time elapsed on the task as well as the
     * number of pomodoro completed. The number of pomodoro completed will be updated only if
     * the number of pomodoro for the task is less than or equal the number of completed pomodoro
     *
     * @param taskIdRunning the id of the task that is running and completed
     * @param targetTimeInMillis the time spent on the task
     */
    private fun updateTask(taskIdRunning: String, targetTimeInMillis: Long) {
        scope.launch {
            val queryTaskResponse: Result<GetTaskUseCase.Response> = getTaskUseCase.execute(
                GetTaskUseCase.Request(taskIdRunning)
            ).first()

            if (queryTaskResponse is Result.Success) {
                val task: Task = queryTaskResponse.data.taskResource.task

                task.pomodoro.apply {
                    if (pomodoroNumber >= pomodoroCompleted + 1) {
                        pomodoroCompleted += 1
                    }
                    elapsedTime += targetTimeInMillis
                }
                val report = Report(
                    completedTime = OffsetDateTime.now(),
                    elapsedTime = targetTimeInMillis,
                    taskId = task.id,
                    tagId = task.tagId,
                    projectId = task.projectId
                )
                upsertTaskUseCase.execute(
                    UpsertTaskUseCase.Request(task)
                )
                insertReportUseCase.execute(
                    InsertReportUseCase.Request(report)
                )
            }
        }
    }
}