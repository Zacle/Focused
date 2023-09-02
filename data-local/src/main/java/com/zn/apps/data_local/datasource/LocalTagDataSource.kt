package com.zn.apps.data_local.datasource

import com.zn.apps.data_local.database.tag.TagDao
import com.zn.apps.data_local.model.asEntity
import com.zn.apps.data_local.model.asExternalModel
import com.zn.apps.data_repository.data_source.local.TagDataSource
import com.zn.apps.model.data.tag.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalTagDataSource @Inject constructor(
    private val tagDao: TagDao
): TagDataSource {

    override fun getTags(): Flow<List<Tag>> {
        return tagDao.getTags().map { tags ->
            tags.map { it.asExternalModel()}
        }
    }

    override fun getTag(tagId: String): Flow<Tag?> {
        return tagDao.getTag(tagId).map { it?.asExternalModel() }
    }

    override suspend fun upsertTag(tag: Tag) {
        tagDao.upsertTag(tag.asEntity())
    }

    override suspend fun deleteTag(tag: Tag) {
        tagDao.deleteTag(tag.asEntity())
    }

    override fun searchTag(query: String): Flow<List<Tag>> {
        return tagDao.searchTag(query).map { tags ->
            tags.map { it.asExternalModel() }
        }
    }
}