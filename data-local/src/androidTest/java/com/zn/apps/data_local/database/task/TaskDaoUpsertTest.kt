package com.zn.apps.data_local.database.task

import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.zn.apps.data_local.database.FocusedDatabase
import com.zn.apps.data_local.database.util.TaskEntities.populatedTaskEntity_3
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_1
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_2
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_3
import com.zn.apps.data_local.database.util.TaskEntities.task_1
import com.zn.apps.data_local.database.util.TaskEntities.task_3
import com.zn.apps.data_local.model.asEntity
import com.zn.apps.model.data.task.TaskPriority
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.OffsetDateTime
import javax.inject.Inject
import javax.inject.Named

@MediumTest
@HiltAndroidTest
class TaskDaoUpsertTest {

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
    fun addTaskTest() = runTest {
        taskDao.upsertTask(taskEntity_1)
        assertThat(taskDao.getTasks(false).first().size).isEqualTo(1)
    }

    @Test
    fun notAddTheSameTaskTwice() = runTest {
        taskDao.upsertTask(taskEntity_2)
        taskDao.upsertTask(taskEntity_2)
        assertThat(taskDao.getTasks(false).first().size).isEqualTo(1)
    }

    @Test
    fun increasePomodoroCompletedByOne() = runTest {
        taskDao.upsertTask(taskEntity_2)
        taskDao.upsertTask(taskEntity_2.apply { pomodoro.pomodoroCompleted += 1 })
        assertThat(
            taskDao.getTask(taskEntity_2.id).first()?.task?.pomodoro?.pomodoroCompleted
        )
            .isEqualTo(1)
    }

    @Test
    fun increaseElapsedTime() = runTest {
        taskDao.upsertTask(taskEntity_2)
        taskDao.upsertTask(taskEntity_2.apply { pomodoro.elapsedTime += 10000 })
        assertThat(
            taskDao.getTask(taskEntity_2.id).first()?.task?.pomodoro?.elapsedTime
        )
            .isEqualTo(10000)
    }

    @Test
    fun changeTaskPriority() = runTest {
        taskDao.upsertTask(taskEntity_3)
        taskDao.upsertTask(task_3.copy(priority = TaskPriority.HIGH).asEntity())
        val response = taskDao.getTask(task_3.id).first()
        assertThat(response?.task?.priority).isEqualTo(TaskPriority.HIGH)
    }

    @Test
    fun updateDueDate() = runTest {
        taskDao.upsertTask(taskEntity_1)
        val dueDate = task_1.copy(dueDate = OffsetDateTime.now())
        taskDao.upsertTask(dueDate.asEntity())
        val response = taskDao.getTask(taskEntity_1.id).first()
        assertThat(response?.task?.dueDate).isNotNull()
    }

    @Test
    fun setTaskCompleted() = runTest {
        taskDao.upsertTask(taskEntity_3)
        val task = task_3.copy(completed = true, completedTime = OffsetDateTime.now()).asEntity()
        taskDao.upsertTask(task)
        val response = taskDao.getTasks(false).first()
        assertThat(response).doesNotContain(populatedTaskEntity_3)
    }
}