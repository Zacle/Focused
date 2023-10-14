package com.zn.apps.datastore

import androidx.datastore.core.DataStore
import com.zn.apps.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FocusedUserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    val userData = userPreferences.data
        .map { userPreferences ->
            UserData(
                shouldHideOnboarding = userPreferences.shouldHideOnboarding
            )
        }

    suspend fun setShouldHideOnboarding(shouldHideOnboarding: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.shouldHideOnboarding = shouldHideOnboarding
            }
        }
    }
}