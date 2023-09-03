package com.zn.apps.focused

import androidx.lifecycle.viewModelScope
import com.zn.apps.domain.datastore.GetUserDataUseCase
import com.zn.apps.model.UserData
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiAction
import com.zn.apps.ui_common.state.UiEvent
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val converter: MainActivityConverter
): BaseViewModel<UserData, UiState<UserData>, UiAction, UiEvent>() {

    override fun initState(): UiState<UserData> = UiState.Loading

    init {
        submitAction(MainActivityUiAction.Load)
    }

    override fun handleAction(action: UiAction) {
        when(action) {
            is MainActivityUiAction.Load -> {
                loadUserData()
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            getUserDataUseCase.execute(GetUserDataUseCase.Request)
                .mapLatest {
                    converter.convert(it)
                }
                .collectLatest { state ->
                    submitState(state)
                }
        }
    }
}