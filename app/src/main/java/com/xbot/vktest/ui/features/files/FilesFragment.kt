package com.xbot.vktest.ui.features.files

import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
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
import com.xbot.vktest.ui.extensions.sharedPref
import com.xbot.vktest.ui.extensions.toIntent
import com.xbot.vktest.ui.extensions.viewBinding
import kotlinx.coroutines.launch

class FilesFragment : Fragment(R.layout.fragment_files), MenuProvider, SortDialog.UpdateDataTrigger {

    private val binding: FragmentFilesBinding by viewBinding(FragmentFilesBinding::bind)
    private val viewModel: FilesViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val args: FilesFragmentArgs by navArgs()
    private val sortArg: Int by sharedPref()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

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

        updateData()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filesList.collect { list ->
                    adapter.submitList(list)
                    updatePlaceholder(list.isEmpty())
                }
            }
        }
    }

    override fun updateData() {
        viewModel.fetchData(args.path.ifEmpty { DEFAULT_PATH }, sortArg)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.files_appbar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_sort -> {
                openSortBottomSheet()
                true
            }
            else -> false
        }
    }

    private fun navigateToDirectory(file: FileItem) {
        val action = FilesFragmentDirections.actionFeatureFilesSelf(file.title, file.path)
        findNavController().navigate(action)
    }

    private fun openSortBottomSheet() {
        val action = FilesFragmentDirections.actionFeatureFilesToBottomSheetSort()
        findNavController().navigate(action)
    }

    private fun openFile(file: FileItem) {
        val intent = file.toIntent(requireContext())
        requireContext().startActivity(intent)
    }

    private fun updatePlaceholder(show: Boolean) {
        binding.recycler.isVisible = !show
        binding.emptyPlaceholder.isVisible = show
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
        val DEFAULT_PATH: String = Environment.getExternalStorageDirectory().path
    }
}