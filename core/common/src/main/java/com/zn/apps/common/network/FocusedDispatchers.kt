package com.zn.apps.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val focusedDispatcher: FocusedDispatchers)

enum class FocusedDispatchers {
    Default,
    IO,
    Main
}