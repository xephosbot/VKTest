package com.xbot.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xbot.data.source.AppDatabase.Companion.DATABASE_VERSION
import com.xbot.data.model.VideoEntity

@Database(
    entities = [VideoEntity::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao

    companion object {
        const val DATABASE_NAME = "videos.db"
        const val DATABASE_VERSION = 1
    }
}