package com.xbot.vktest.di

import android.content.Context
import androidx.room.Room
import com.xbot.vktest.data.AppDatabase
import com.xbot.vktest.data.files.FilesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database-name").build()
    }

    @Singleton
    @Provides
    fun provideFilesRepository(
        database: AppDatabase,
        @ApplicationContext context: Context
    ) = FilesRepository(database.fileDao(), context)
}