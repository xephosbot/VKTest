package com.xbot.vktest.di

import com.xbot.home.HomeViewModel
import com.xbot.player.PlayerViewModel
import com.xbot.player.service.PlaybackService
import com.xbot.player.service.PlayerProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    factory { PlayerProvider.create<PlaybackService>(context = androidContext()) }
    viewModelOf(::HomeViewModel)
    viewModelOf(::PlayerViewModel)
}