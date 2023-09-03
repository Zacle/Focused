package com.zn.apps.feature.onboarding

import androidx.lifecycle.viewModelScope
import com.zn.apps.domain.datastore.SetShouldHideOnboardingUseCase
import com.zn.apps.feature.onboarding.data.OnboardingPage
import com.zn.apps.ui_common.state.BaseViewModel
import com.zn.apps.ui_common.state.UiEvent
import com.zn.apps.ui_common.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val setShouldHideOnboardingUseCase: SetShouldHideOnboardingUseCase
): BaseViewModel<OnboardedPageModel, UiState<OnboardedPageModel>, OnboardingUiAction, UiEvent>() {

    override fun initState(): UiState<OnboardedPageModel> = UiState.Loading

    init {
        submitAction(OnboardingUiAction.Load)
    }

    override fun handleAction(action: OnboardingUiAction) {
        when(action) {
            is OnboardingUiAction.Load -> {
                loadOnboardingPages()
            }
            is OnboardingUiAction.SetOnboarded -> {
                setUserOnboarded()
            }
        }
    }

    private fun loadOnboardingPages() {
        submitState(
            UiState.Success(
                OnboardedPageModel(
                    pages = listOf(
                        OnboardingPage.ScheduleTasks,
                        OnboardingPage.RunTasks,
                        OnboardingPage.OrganizeTasks
                    )
                )
            )
        )
    }

    private fun setUserOnboarded() {
        viewModelScope.launch {
            setShouldHideOnboardingUseCase.execute(
                SetShouldHideOnboardingUseCase.Request(true)
            )
        }
    }
}