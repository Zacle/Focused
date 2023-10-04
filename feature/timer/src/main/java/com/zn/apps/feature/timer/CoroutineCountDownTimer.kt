package com.zn.apps.feature.timer

import com.zn.apps.common.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoroutineCountDownTimer @Inject constructor(
    @ApplicationScope private val scope: CoroutineScope,
    private val timeElapsedInMillis: ElapsedTime
): CountDownTimer {

    private var timeLeftInMillis: Long = 0L

    private var timerJob: Job? = null

    override fun startTimer(
        durationInMillis: Long,
        countDownInterval: Long,
        onTick: suspend (timeLeftInMillis: Long) -> Unit,
        onFinished: suspend () -> Unit
    ) {
        timeLeftInMillis = durationInMillis
        val startTime = timeElapsedInMillis.elapsedTime
        val endTime = startTime + durationInMillis
        timerJob = scope.launch {
            while (true) {
                if (timeElapsedInMillis.elapsedTime < endTime) {
                    delay(countDownInterval)
                    timeLeftInMillis = endTime - timeElapsedInMillis.elapsedTime
                    /** Make sure the delay is equal of [countDownInterval] **/
                    if ((timeLeftInMillis + countDownInterval) != timeElapsedInMillis.elapsedTime)
                        timeLeftInMillis += countDownInterval
                    onTick(timeLeftInMillis)
                } else {
                    onFinished()
                    break
                }
            }
        }
    }

    override fun stopTimer() {
        timeLeftInMillis = 0L
        timerJob?.cancel()
        timerJob = null
    }
}