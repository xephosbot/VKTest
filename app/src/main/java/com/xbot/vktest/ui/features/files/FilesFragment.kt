package com.xbot.vktest.ui.features.files

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.xbot.vktest.R
import com.xbot.vktest.databinding.FragmentFilesBinding
import com.xbot.vktest.ui.RecyclerViewAdapter
import com.xbot.vktest.ui.extensions.viewBinding
import kotlinx.coroutines.launch

class FilesFragment : Fragment(R.layout.fragment_files) {

    private val binding: FragmentFilesBinding by viewBinding(FragmentFilesBinding::bind)
    private val viewModel: FilesViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RecyclerViewAdapter { file ->
            //TODO: On click
        }
        binding.recycler.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(binding.recycler) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.recycler.updatePadding(bottom = insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filesList.collect { list ->
                    adapter.submitList(list)
                }
            }
        }
    }
}