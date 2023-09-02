package com.zn.apps.data_repository.repository

import com.zn.apps.datastore.FocusedUserPreferencesDataSource
import com.zn.apps.domain.repository.UserDataRepository
import com.zn.apps.model.UserData
import kotlinx.coroutines.flow.Flow

class DefaultUserDataRepository(
    private val focusedUserPreferencesDataSource: FocusedUserPreferencesDataSource
): UserDataRepository {

    override val userData: Flow<UserData> =
        focusedUserPreferencesDataSource.userData

    override suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        focusedUserPreferencesDataSource.setShouldHideOnboarding(shouldHideOnboarding)
    }
}