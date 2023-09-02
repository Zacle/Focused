package com.zn.apps.data_local.database.tag

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tag",
    indices = [Index(value = ["name"], unique = true)]
)
data class TagEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "color")
    val color: Int? = null
) {

    /** Only IDs are used for equality. */
    override fun equals(other: Any?): Boolean = this === other || (other is TagEntity && other.id == id)

    /** Only IDs are used for equality. */
    override fun hashCode(): Int = id.hashCode()

}
