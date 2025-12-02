package com.example.playlistmaker.ui.library.playlist.create_update_playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.view_model.library.playlist.NewPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

open class NewPlaylistFragment : BasePlaylistCreator() {


    override val viewModel: NewPlaylistViewModel by viewModel { parametersOf(null) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { isNewPlaylistAdded ->
                if (isNewPlaylistAdded.first) showNewPlaylist(isNewPlaylistAdded.second)
            }
        }
    }

    override fun showExitDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.dialog_title))
            .setMessage(requireContext().getString(R.string.dialog_message))
            .setNegativeButton(requireContext().getString(R.string.dialog_cancel)) { dialog, which -> dialog.cancel() }
            .setPositiveButton(requireContext().getString(R.string.dialog_ok)) { dialog, which -> findNavController().popBackStack() }
            .show()
    }


    override fun onFinishButtonClicked() {
        val name = binding.etName.text.toString()
        val description = binding.etDescription.text.toString()
        if (name.isNotBlank()) viewModel.addNewPlaylist(name, description, selectedImageUri)
    }

    @SuppressLint("RestrictedApi")
    fun showNewPlaylist(name: String) {
        val message = requireContext().getString(R.string.toast_playlist_created)
        val messageFormatted = String.format(message, name)

        val parent = requireActivity().findViewById<ViewGroup>(R.id.mainContainerView)

        val snackBar = Snackbar.make(parent, "", Snackbar.LENGTH_SHORT)

        val snackbarView = LayoutInflater.from(requireActivity()).inflate(
            R.layout.snackbar_new_playlist,
            parent,
            false
        )

        snackbarView.findViewById<TextView>(R.id.tvText).text = messageFormatted

        val snackbarLayout = snackBar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(snackbarView)

        snackBar.show()

        findNavController().popBackStack()
    }
}