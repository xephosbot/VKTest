package com.xbot.player

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xbot.player.service.PlaybackService
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : ComponentActivity() {
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                val player by viewModel.controller.collectAsStateWithLifecycle()
                PlayerScreen(
                    player = player,
                    onCloseClick = {
                        closeActivity()
                    },
                )
            }
        }

        onBackPressedDispatcher.addCallback {
            closeActivity()
        }
    }

    private fun closeActivity() {
        Intent(this@PlayerActivity, PlaybackService::class.java).apply {
            action = PlaybackService.STOP_ACTION
            startService(this)
        }
        finish()
    }
}