package com.zn.apps.feature.timer.di

import com.zn.apps.common.network.di.ApplicationScope
import com.zn.apps.feature.timer.CoroutineCountDownTimer
import com.zn.apps.feature.timer.CountDownTimer
import com.zn.apps.feature.timer.ElapsedRealTime
import com.zn.apps.feature.timer.ElapsedTime
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