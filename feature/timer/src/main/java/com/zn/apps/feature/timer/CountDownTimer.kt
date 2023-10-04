package com.zn.apps.feature.timer

interface CountDownTimer {
    fun startTimer(
        durationInMillis: Long,
        countDownInterval: Long,
        onTick: suspend (timeLeftInMillis: Long) -> Unit,
        onFinished: suspend () -> Unit
    )

    fun stopTimer()
}