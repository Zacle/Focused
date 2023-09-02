package com.zn.apps.model.data.task

/**
 * Pomodoro is used to handle how the user want to achieve the given
 * task by breaking the estimated time into more manageable time to boost
 * the concentration
 *
 * @param pomodoroNumber the number of times to focus on the task without interruptions
 * @param pomodoroLength the time length to focus in milliseconds, default to 30 minutes
 * @param pomodoroCompleted how many time we worked without interruption
 * @param elapsedTime the total time spent on the task (finished or stopped)
 */
data class Pomodoro(
    var pomodoroNumber: Int = 0,
    var pomodoroLength: Long = 0,
    var pomodoroCompleted: Int = 0,
    var elapsedTime: Long = 0
) {

    /**
     * Return the remaining estimated time to complete the task
     */
    fun getEstimatedRemainingTime(): Long {
        /* return the positive number of [pomodoroNumber] to avoid multiplication with zero */
        val positivePomodoroNumber = if (pomodoroNumber <= 0) 1 else pomodoroNumber

        return (positivePomodoroNumber - pomodoroCompleted) * pomodoroLength
    }

}
