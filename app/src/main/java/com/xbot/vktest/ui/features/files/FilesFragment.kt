package com.xbot.vktest.ui.features.files

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.xbot.vktest.R
import com.xbot.vktest.databinding.FragmentFilesBinding
import com.xbot.vktest.model.FileItem
import com.xbot.vktest.model.FileType
import com.xbot.vktest.ui.RecyclerViewAdapter
import com.xbot.vktest.ui.extensions.toIntent
import com.xbot.vktest.ui.extensions.viewBinding
import kotlinx.coroutines.launch

class FilesFragment : Fragment(R.layout.fragment_files) {

    private val binding: FragmentFilesBinding by viewBinding(FragmentFilesBinding::bind)
    private val viewModel: FilesViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val args: FilesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RecyclerViewAdapter { file ->
            when(file.type) {
                FileType.FOLDER -> navigateToDirectory(file)
                else -> openFile(file)
            }
        }
        adapter.stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        val layoutManager = binding.recycler.layoutManager as LinearLayoutManager

        binding.recycler.adapter = adapter
        binding.recycler.addItemDecoration(getMaterialDivider(layoutManager))

        ViewCompat.setOnApplyWindowInsetsListener(binding.recycler) { _, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.recycler.updatePadding(bottom = insets.bottom)
            WindowInsetsCompat.CONSUMED
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getFilesList(args.path).collect { list ->
                    adapter.submitList(list)
                }
            }
        }
    }

    private fun navigateToDirectory(file: FileItem) {
        findNavController().navigate(
            resId = DESTINATION_ID,
            args = bundleOf(ARG_TITLE to file.title, ARG_PATH to file.path)
        )
    }

    private fun openFile(file: FileItem) {
        val intent = file.toIntent(requireContext())
        requireContext().startActivity(intent)
    }

    private fun getMaterialDivider(layoutManager: LinearLayoutManager): MaterialDividerItemDecoration {
        return MaterialDividerItemDecoration(requireContext(), layoutManager.orientation).apply {
            dividerInsetStart = resources.getDimension(R.dimen.horizontal_padding).toInt() * 2 +
                    resources.getDimension(R.dimen.leading_icon_size).toInt()
            dividerInsetEnd = resources.getDimension(R.dimen.trailing_padding).toInt()
            isLastItemDecorated = false
        }
    }

    private companion object {
        val DESTINATION_ID: Int = R.id.feature_files
        const val ARG_TITLE = "title"
        const val ARG_PATH = "path"
    }
}