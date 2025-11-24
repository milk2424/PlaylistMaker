package com.example.playlistmaker.ui.library.playlist

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.presentation.mapper.player_mapper.DpToPxMapper
import com.example.playlistmaker.presentation.view_model.library.playlist.NewPlaylistViewModel
import com.example.playlistmaker.ui.FragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlaylistFragment : FragmentBinding<FragmentNewPlaylistBinding>() {

    private var selectedImageUri: Uri? = null

    private val viewModel: NewPlaylistViewModel by viewModel()

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentNewPlaylistBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(requireContext()).load(uri).transform(
                        CenterCrop(), RoundedCorners(
                            DpToPxMapper.map(8f, requireContext())
                        )
                    ).into(binding.btnAddPlaylistImg)
                    selectedImageUri = uri
                }
            }


        binding.btnAddPlaylistImg.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.etName.doOnTextChanged { text, p1, p2, p3 ->
            binding.btnCreate.isEnabled = text?.isNotBlank() ?: false
        }

        binding.btnCreate.setOnClickListener {
            createNewPlaylist()
        }

        binding.btnBack.setOnClickListener {
            canUserGoBack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    canUserGoBack()
                }
            }
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { isNewPlaylistAdded ->
                if (isNewPlaylistAdded.first) showNewPlaylist(isNewPlaylistAdded.second)
            }
        }
    }

    private fun canUserGoBack() {
        if (!binding.etName.text.isNullOrBlank() || !binding.etDescription.text.isNullOrBlank() || selectedImageUri != null) showExitDialog()
        else findNavController().popBackStack()
    }

    private fun showExitDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireContext().getString(R.string.dialog_title))
            .setMessage(requireContext().getString(R.string.dialog_message))
            .setNegativeButton(requireContext().getString(R.string.dialog_cancel)) { dialog, which -> dialog.cancel() }
            .setPositiveButton(requireContext().getString(R.string.dialog_ok)) { dialog, which -> findNavController().popBackStack() }
            .show()
    }


    private fun createNewPlaylist() {
        val name = binding.etName.text.toString()
        val description = binding.etDescription.text.toString()
        if (name.isNotBlank()) viewModel.addNewPlaylist(name, description, selectedImageUri)
    }

    @SuppressLint("RestrictedApi")
    private fun showNewPlaylist(name: String) {
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