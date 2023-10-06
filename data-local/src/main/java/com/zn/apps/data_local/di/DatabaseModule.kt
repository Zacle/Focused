package com.zn.apps.data_local.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zn.apps.common.network.Dispatcher
import com.zn.apps.common.network.FocusedDispatchers.IO
import com.zn.apps.data_local.database.DatabaseMigrations.MIGRATION_1_2
import com.zn.apps.data_local.database.FocusedDatabase
import com.zn.apps.data_local.database.ioThread
import com.zn.apps.data_local.database.project.ProjectDao
import com.zn.apps.data_local.database.report.ReportDao
import com.zn.apps.data_local.database.tag.TagDao
import com.zn.apps.data_local.database.task.TaskDao
import com.zn.apps.data_local.model.INITIAL_TAGS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesFocusedDatabase(
        @ApplicationContext context: Context,
        @Dispatcher(IO) dispatcher: CoroutineDispatcher,
        tagDaoProvider: Provider<TagDao>
    ): FocusedDatabase =
        Room.databaseBuilder(
            context,
            FocusedDatabase::class.java,
            "focused-db"
        )
            .addCallback(
                object: RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        val applicationScope = CoroutineScope(SupervisorJob())

                        ioThread {
                            applicationScope.launch(dispatcher) {
                                tagDaoProvider.get().insertAllTags(INITIAL_TAGS)
                            }
                        }
                    }
                }
            )
            .addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    fun providesTaskDao(
        database: FocusedDatabase
    ): TaskDao = database.taskDao()

    @Provides
    fun providesTagDao(
        database: FocusedDatabase
    ): TagDao = database.tagDao()

    @Provides
    fun providesProjectDao(
        database: FocusedDatabase
    ): ProjectDao = database.projectDao()

    @Provides
    fun providesReportDao(
        database: FocusedDatabase
    ): ReportDao = database.reportDao()
}