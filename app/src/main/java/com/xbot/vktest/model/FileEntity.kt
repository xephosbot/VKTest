package com.xbot.vktest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files")
data class FileEntity(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "file_path") val path: String
)