package com.xbot.vktest.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xbot.vktest.data.files.FileDao
import com.xbot.vktest.model.FileEntity

@Database(entities = [FileEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fileDao(): FileDao
}