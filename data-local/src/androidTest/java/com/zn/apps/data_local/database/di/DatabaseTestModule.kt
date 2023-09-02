package com.zn.apps.data_local.database.di

import android.content.Context
import androidx.room.Room
import com.zn.apps.data_local.database.FocusedDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object DatabaseTestModule {
    @Provides
    @Named("test_db")
    fun providesInMemoryDatabase(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, FocusedDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}