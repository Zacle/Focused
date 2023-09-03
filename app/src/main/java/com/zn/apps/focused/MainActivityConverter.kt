package com.zn.apps.focused

import android.content.Context
import com.zn.apps.domain.datastore.GetUserDataUseCase
import com.zn.apps.model.R
import com.zn.apps.model.UserData
import com.zn.apps.model.usecase.UseCaseException
import com.zn.apps.ui_common.state.CommonResultConverter
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MainActivityConverter @Inject constructor(
    @ApplicationContext private val context: Context
): CommonResultConverter<GetUserDataUseCase.Response, UserData>() {
    override fun convertSuccess(data: GetUserDataUseCase.Response): UserData = data.userData

    override fun convertError(useCaseException: UseCaseException): String =
        context.getString(R.string.unknown_error)
}