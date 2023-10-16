package com.zn.apps.core.timer.di

import com.zn.apps.common.network.di.ApplicationScope
import com.zn.apps.core.timer.CoroutineCountDownTimer
import com.zn.apps.core.timer.CountDownTimer
import com.zn.apps.core.timer.ElapsedRealTime
import com.zn.apps.core.timer.ElapsedTime
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object TimerModule {

    @Provides
    fun providesCountDownTimer(
        @ApplicationScope scope: CoroutineScope,
        elapsedTime: ElapsedTime
    ): CountDownTimer = CoroutineCountDownTimer(scope, elapsedTime)

    @Provides
    fun providesElapsedRealTime(): ElapsedTime = ElapsedRealTime()
}