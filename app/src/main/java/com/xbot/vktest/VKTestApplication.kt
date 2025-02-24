package com.xbot.vktest

import android.app.Application
import com.xbot.api.di.apiModule
import com.xbot.data.di.dataModule
import com.xbot.vktest.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class VKTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@VKTestApplication)
            modules(apiModule, dataModule, viewModelModule)
        }
    }
}