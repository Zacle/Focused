package com.zn.apps.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.zn.apps.common.network.Dispatcher
import com.zn.apps.common.network.FocusedDispatchers.IO
import com.zn.apps.common.network.di.ApplicationScope
import com.zn.apps.datastore.FocusedUserPreferencesDataSource
import com.zn.apps.datastore.UserPreferences
import com.zn.apps.datastore.UserPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @Dispatcher(IO) dispatcher: CoroutineDispatcher,
        @ApplicationScope scope: CoroutineScope,
        userPreferencesSerializer: UserPreferencesSerializer
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            scope = CoroutineScope(scope.coroutineContext + dispatcher)
        ) {
            context.dataStoreFile("user_preferences.pb")
        }

    @Provides
    @Singleton
    fun providesFocusedUserPreferencesDataSource(
        userPreferences: DataStore<UserPreferences>
    ): FocusedUserPreferencesDataSource = FocusedUserPreferencesDataSource(userPreferences)
}