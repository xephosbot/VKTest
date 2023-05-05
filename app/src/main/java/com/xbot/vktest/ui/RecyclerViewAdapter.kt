package com.xbot.vktest.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xbot.vktest.databinding.RecyclerItemBinding
import com.xbot.vktest.model.File
import com.xbot.vktest.ui.extensions.viewBinding

class RecyclerViewAdapter(private val onClick: (File) -> Unit) :
    ListAdapter<File, RecyclerViewAdapter.FileViewHolder>(DiffCallback) {

    class FileViewHolder(
        private val binding: RecyclerItemBinding,
        private val onClick: (File) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentFile: File? = null

        init {
            binding.root.setOnClickListener {
                currentFile?.let { onClick(it) }
            }
        }

        fun bind(file: File) {
            currentFile = file
            binding.title.text = file.title
            binding.subtitle.text = file.date
            binding.trailingText.text = file.size
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

    object DiffCallback : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem == newItem
        }
    }
}