package com.xbot.vktest.di

import android.content.Context
import com.xbot.vktest.data.FilesRepository
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
    fun provideFilesRepository(@ApplicationContext context: Context) = FilesRepository(context)
}