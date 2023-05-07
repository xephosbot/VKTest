package com.xbot.vktest.ui

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.xbot.vktest.R
import com.xbot.vktest.databinding.ActivityMainBinding
import com.xbot.vktest.ui.extensions.setupWithNavController
import com.xbot.vktest.ui.extensions.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment)

        binding.toolbar.setupWithNavController(navController)
    }

    private fun findNavController(@IdRes id: Int): NavController {
        val navHostFragment = supportFragmentManager.findFragmentById(id) as NavHostFragment
        return navHostFragment.navController
    }
}