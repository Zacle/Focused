package com.zn.apps.data_local.database.tag

import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import com.zn.apps.data_local.database.FocusedDatabase
import com.zn.apps.data_local.database.task.TaskDao
import com.zn.apps.data_local.database.util.TaskEntities.designTag
import com.zn.apps.data_local.database.util.TaskEntities.otherTag
import com.zn.apps.data_local.database.util.TaskEntities.sportTag
import com.zn.apps.data_local.database.util.TaskEntities.studyTag
import com.zn.apps.data_local.database.util.TaskEntities.taskEntity_5
import com.zn.apps.data_local.database.util.TaskEntities.workTag
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
class TagDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: FocusedDatabase

    private lateinit var tagDao: TagDao
    private lateinit var taskDao: TaskDao

    @Before
    fun setup() {
        hiltRule.inject()
        tagDao = database.tagDao()
        taskDao = database.taskDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun shouldDeleteTag() = runTest {
        tagDao.upsertTag(designTag)
        assertThat(tagDao.getTags().first()).contains(designTag)
        tagDao.deleteTag(designTag)
        assertThat(tagDao.getTags().first()).doesNotContain(designTag)
    }

    @Test
    fun retrieveAllTags() = runTest {
        tagDao.insertAllTags(listOf(studyTag, sportTag, designTag, otherTag, workTag))
        assertThat(tagDao.getTags().first().size).isEqualTo(5)
    }

    @Test
    fun retrieveEmptyList() = runTest {
        assertThat(tagDao.getTags().first()).isEmpty()
    }

    @Test
    fun retrieveTagMatchingId() = runTest {
        val id = studyTag.id
        tagDao.insertAllTags(listOf(studyTag, sportTag, designTag, otherTag, workTag))
        val result = tagDao.getTag(id).first()
        assertThat(result).isEqualTo(studyTag)
    }

    @Test
    fun returnNullIfNotMatchingIdFound() = runTest {
        val id = "nonexistent"
        tagDao.insertAllTags(listOf(studyTag, sportTag, designTag, otherTag, workTag))
        val result = tagDao.getTag(id).first()
        assertThat(result).isEqualTo(null)
    }

    @Test
    fun shouldAddTag() = runTest {
        tagDao.insertAllTags(listOf(studyTag, sportTag, designTag, otherTag))
        assertThat(tagDao.getTags().first().size).isEqualTo(4)
        tagDao.upsertTag(workTag)
        assertThat(tagDao.getTags().first().size).isEqualTo(5)
    }

    @Test
    fun shouldNotAddAnExistingTag() = runTest {
        tagDao.insertAllTags(listOf(studyTag, sportTag, designTag, otherTag))
        assertThat(tagDao.getTags().first().size).isEqualTo(4)
        tagDao.upsertTag(otherTag)
        assertThat(tagDao.getTags().first().size).isEqualTo(4)
    }

    @Test
    fun shouldModifyTagNameIfNotExistInTheTable() = runTest {
        val name = "design"
        tagDao.insertAllTags(listOf(studyTag, sportTag, designTag))
        val tag = designTag.copy(name = name)
        tagDao.upsertTag(tag)
        val updatedTag = tagDao.getTag(designTag.id).first()
        assertThat(updatedTag?.name).isEqualTo(name)
    }

    @Test
    fun shouldNotModifyTagNameIfAlreadyExistInTheTable() = runTest {
        val name = "sportTag"
        tagDao.insertAllTags(listOf(studyTag, sportTag, designTag))
        val tag = designTag.copy(name = name)
        tagDao.upsertTag(tag)
        val updatedTag = tagDao.getTag(designTag.id).first()
        assertThat(updatedTag?.name).isNotEqualTo(name)
        assertThat(updatedTag?.name).isEqualTo("designTag")
    }

    @Test
    fun returnTagThatMatchPartialQuery() = runTest {
        val query = "s"
        tagDao.insertAllTags(listOf(studyTag, sportTag, designTag, otherTag))
        val result = tagDao.searchTag(query).first()
        assertThat(result).isEqualTo(listOf(studyTag, sportTag, designTag))
    }

    @Test
    fun returnTagThatMatchTotalQuery() = runTest {
        val query = "sportTag"
        tagDao.insertAllTags(listOf(studyTag, sportTag, designTag, otherTag))
        val result = tagDao.searchTag(query).first()
        assertThat(result).isEqualTo(listOf(sportTag))
    }

    @Test
    fun returnEmptyListWhenNoQueryMatch() = runTest {
        val query = "noQuery"
        tagDao.insertAllTags(listOf(studyTag, sportTag, designTag, otherTag))
        val result = tagDao.searchTag(query).first()
        assertThat(result).isEmpty()
    }

    @Test
    fun shouldAssociateTaskWithTag() = runTest {
        tagDao.insertAllTags(listOf(studyTag, sportTag, designTag, otherTag))
        val taskEntity = taskEntity_5.copy(tagId = designTag.id)
        taskDao.upsertTask(taskEntity)
        val populatedTask = taskDao.getTask(taskEntity.id).first()
        assertThat(populatedTask?.tag).isNotEmpty()
    }

    @Test
    fun shouldRemoveAssociatedTag() = runTest {
        tagDao.insertAllTags(listOf(studyTag, sportTag, designTag, otherTag))
        val taskEntity = taskEntity_5.copy(tagId = designTag.id)
        taskDao.upsertTask(taskEntity)
        val removeTag = taskEntity.copy(tagId = null)
        taskDao.upsertTask(removeTag)
        val populatedTask = taskDao.getTask(removeTag.id).first()
        assertThat(populatedTask?.tag).isEmpty()
    }
}