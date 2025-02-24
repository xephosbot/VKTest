package com.xbot.vktest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.xbot.vktest.ui.VKTestApp
import com.xbot.vktest.ui.theme.VKTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VKTestTheme {
                VKTestApp()
            }
        }
    }
}
