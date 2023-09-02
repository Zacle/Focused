package com.zn.apps.data_local.database.tag

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {

    @Query("SELECT * FROM tag")
    fun getTags(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tag WHERE id = :tagId")
    fun getTag(tagId: String): Flow<TagEntity?>

    @Insert
    suspend fun insertAllTags(tagLIst: List<TagEntity>)

    @Insert
    suspend fun insertTag(tag: TagEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateTag(tag: TagEntity)

    @Delete
    suspend fun deleteTag(tag: TagEntity)

    @Query("SELECT * FROM tag WHERE name LIKE '%' || :nameToSearch || '%'")
    fun searchTag(nameToSearch: String): Flow<List<TagEntity>>

    suspend fun upsertTag(tag: TagEntity) {
        try {
            insertTag(tag)
        } catch (e: SQLiteConstraintException) {
            updateTag(tag)
        }
    }
}