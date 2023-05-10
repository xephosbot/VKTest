package com.xbot.vktest.data.files

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xbot.vktest.model.FileEntity

@Dao
interface FileDao {

    @Query("SELECT * FROM files")
    suspend fun getAll(): List<FileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(files: List<FileEntity>)
}