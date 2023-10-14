package com.zn.apps.domain.repository

import com.zn.apps.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets whether the user has completed the onboarding process
     */
    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean)
}