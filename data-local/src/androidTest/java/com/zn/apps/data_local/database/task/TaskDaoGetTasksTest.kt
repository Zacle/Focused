package com.zn.apps.data_local.database.task

import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.zn.apps.data_local.database.FocusedDatabase
import com.zn.apps.data_local.database.util.TaskEntities.populatedTaskEntity_1
import com.zn.apps.data_local.database.util.TaskEntities.populatedTaskEntity_4
import com.zn.apps.data_local.database.util.TaskEntities.populatedTaskEntity_5
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_1
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_2
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_3
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_4
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_5
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
class TaskDaoGetTasksTest {

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
    fun returnEmptyWhenNoTasksAdded() = runTest {
        assertThat(taskDao.getTasks(false).first()).isEmpty()
    }

    @Test
    fun returnAnElementThatMatchesTheQuery() = runTest {
        taskDao.insertTask(taskEntity_1)
        assertThat(taskDao.getTasks(completed = false).first())
            .isEqualTo(listOf(populatedTaskEntity_1))
    }

    @Test
    fun returnEmptyListWhenNoMatchFound() = runTest {
        taskDao.insertTask(taskEntity_2)
        assertThat(taskDao.getTasks(completed = true).first()).isEmpty()
    }

    @Test
    fun returnListWhenMatchFound() = runTest {
        taskDao.insertTasks(
            listOf(taskEntity_1, taskEntity_2, taskEntity_3, taskEntity_4, taskEntity_5)
        )
        assertThat(taskDao.getTasks(true).first())
            .isEqualTo(listOf(populatedTaskEntity_4, populatedTaskEntity_5))
    }
}