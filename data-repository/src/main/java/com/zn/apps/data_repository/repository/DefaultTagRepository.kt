package com.zn.apps.data_repository.repository

import com.zn.apps.data_repository.data_source.local.TagDataSource
import com.zn.apps.domain.repository.TagRepository
import com.zn.apps.model.data.tag.Tag
import kotlinx.coroutines.flow.Flow

class DefaultTagRepository(
    private val tagDataSource: TagDataSource
): TagRepository {

    override fun getTags(): Flow<List<Tag>> {
        return tagDataSource.getTags()
    }

    override fun getTag(tagId: String): Flow<Tag?> {
        return tagDataSource.getTag(tagId)
    }

    override suspend fun upsertTag(tag: Tag) {
        tagDataSource.upsertTag(tag)
    }

    override suspend fun deleteTag(tag: Tag) {
        tagDataSource.deleteTag(tag)
    }

    override fun searchTag(query: String): Flow<List<Tag>> {
        return tagDataSource.searchTag(query)
    }
}