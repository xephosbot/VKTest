package com.xbot.vktest.ui.features.files

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xbot.vktest.R
import com.xbot.vktest.ui.extensions.sharedPref

class SortDialog : DialogFragment() {

    private var sortArg: Int by sharedPref()
    private lateinit var trigger: UpdateDataTrigger

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            trigger = parentFragmentManager.fragments.first() as UpdateDataTrigger
        } catch (e: ClassCastException) {
            throw ClassCastException(parentFragmentManager.fragments.first().toString() + " must implement UpdateDataTrigger")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = findNavController().currentDestination?.label
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setSingleChoiceItems(R.array.sort_keys, sortArg) { dialog, which ->
                sortArg = which
                trigger.updateData()
                dialog.dismiss()
            }

        return dialog.create()
    }

    interface UpdateDataTrigger {
        fun updateData()
    }
}