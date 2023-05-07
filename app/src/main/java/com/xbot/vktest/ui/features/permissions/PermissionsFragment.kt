package com.xbot.vktest.ui.features.permissions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xbot.vktest.R
import com.xbot.vktest.databinding.FragmentPermissionsBinding
import com.xbot.vktest.ui.extensions.viewBinding

class PermissionsFragment : Fragment(R.layout.fragment_permissions) {

    private val binding: FragmentPermissionsBinding by viewBinding(FragmentPermissionsBinding::bind)
    private val requestAllFilesAccessLauncher = registerForActivityResult(StartActivityForResult()) { _ ->
        if (checkPermission()) navigateToFiles()
    }
    private val requestPermissionLauncher = registerForActivityResult(RequestPermission()) { isGranted ->
        if (isGranted) navigateToFiles()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkPermission()) navigateToFiles()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.permissionsButton.setOnClickListener {
            requestPermission()
        }
    }

    private fun navigateToFiles() {
        val action = PermissionsFragmentDirections.actionFeaturePermissionsToFeatureFiles(
            path = DEFAULT_PATH
        )
        findNavController().navigate(action)
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result = ContextCompat.checkSelfPermission(requireContext(),
                READ_EXTERNAL_STORAGE_PERMISSION
            )
            result == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + requireActivity().packageName)
                requestAllFilesAccessLauncher.launch(intent)
            } catch (e: Exception) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                requestAllFilesAccessLauncher.launch(intent)
            }
        } else {
            requestPermissionLauncher.launch(READ_EXTERNAL_STORAGE_PERMISSION)
        }
    }

    private companion object {
        val DEFAULT_PATH: String = Environment.getExternalStorageDirectory().path
        private const val READ_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
    }
}