package com.zn.apps.data_local.database.task

import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.zn.apps.data_local.database.FocusedDatabase
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_1
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@MediumTest
@HiltAndroidTest
class TaskDaoDeleteTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: FocusedDatabase

    private lateinit var taskDao: TaskDao

    @Before
    fun setup() {
        hiltRule.inject()
        taskDao = database.taskDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun shouldBeAbleToDeleteTask() = runTest {
        taskDao.upsertTask(taskEntity_1)
        taskDao.deleteTask(taskEntity_1)
        assertThat(taskDao.getTask(taskEntity_1.id).first()).isNull()
    }
}