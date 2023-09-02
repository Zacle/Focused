package com.zn.apps.domain.repository

import com.zn.apps.model.data.tag.Tag
import kotlinx.coroutines.flow.Flow

interface TagRepository {

    /** Get all tags */
    fun getTags(): Flow<List<Tag>>

    /**
     * Get the tag that matches the given id
     *
     * @param tagId
     */
    fun getTag(tagId: String): Flow<Tag?>

    /**
     * Update the tag if it already exists or insert to the database otherwise
     *
     * @param tag
     */
    suspend fun upsertTag(tag: Tag)

    /**
     * Delete the tag from the database
     *
     * @param tag
     */
    suspend fun deleteTag(tag: Tag)

    /**
     * Given a query (prefix or name), returns all tag that match the query
     *
     * @param query
     */
    fun searchTag(query: String): Flow<List<Tag>>
}