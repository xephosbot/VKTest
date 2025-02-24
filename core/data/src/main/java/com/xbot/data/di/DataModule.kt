package com.xbot.data.di

import androidx.room.Room
import com.xbot.data.repository.OfflineFirstVideoRepository
import com.xbot.data.source.AppDatabase
import com.xbot.data.source.VideoDao
import com.xbot.data.utils.ConnectivityObserver
import com.xbot.data.utils.DefaultConnectivityObserver
import com.xbot.domain.repository.VideoRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }
    single<VideoDao> { get<AppDatabase>().videoDao() }
    singleOf(::DefaultConnectivityObserver) { bind<ConnectivityObserver>() }
    singleOf(::OfflineFirstVideoRepository) { bind<VideoRepository>() }
}