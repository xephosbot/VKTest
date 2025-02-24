package com.xbot.data.utils

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isNetworkAvailable: Flow<Boolean>
}