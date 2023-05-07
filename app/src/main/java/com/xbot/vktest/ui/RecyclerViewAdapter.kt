package com.xbot.vktest.ui

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xbot.vktest.databinding.RecyclerItemBinding
import com.xbot.vktest.model.FileItem
import com.xbot.vktest.ui.extensions.toImageResource
import com.xbot.vktest.ui.extensions.viewBinding


class RecyclerViewAdapter(
    private val onClick: (FileItem) -> Unit
) : ListAdapter<FileItem, RecyclerViewAdapter.FileViewHolder>(DiffCallback) {

    class FileViewHolder(
        private val binding: RecyclerItemBinding,
        private val onClick: (FileItem) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentFile: FileItem? = null

        init {
            binding.root.setOnClickListener {
                currentFile?.let { onClick(it) }
            }
        }

        fun bind(file: FileItem) {
            currentFile = file
            binding.title.text = file.title
            binding.subtitle.text = file.date
            binding.trailingText.text = file.size
            binding.leadingIcon.setImageResource(file.type.toImageResource())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val binding = viewBinding(parent, RecyclerItemBinding::inflate)
        return FileViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = getItem(position)
        holder.bind(file)
    }

    object DiffCallback : DiffUtil.ItemCallback<FileItem>() {
        override fun areItemsTheSame(oldItem: FileItem, newItem: FileItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FileItem, newItem: FileItem): Boolean {
            return oldItem == newItem
        }
    }
}