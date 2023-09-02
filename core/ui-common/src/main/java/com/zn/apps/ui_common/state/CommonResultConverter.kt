package com.zn.apps.ui_common.state

import com.zn.apps.model.usecase.Result.Error
import com.zn.apps.model.usecase.Result.Success
import com.zn.apps.model.usecase.UseCaseException


abstract  class CommonResultConverter<T: Any, R: Any> {

    fun convert(result: com.zn.apps.model.usecase.Result<T>): UiState<R> {
        return when(result) {
            is Error -> {
                UiState.Error(convertError(result.exception))
            }
            is Success -> {
                UiState.Success(convertSuccess(result.data))
            }
        }
    }

    abstract fun convertSuccess(data: T): R

    abstract fun convertError(useCaseException: UseCaseException): String
}