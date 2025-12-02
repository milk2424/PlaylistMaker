package com.example.playlistmaker.ui.library.playlist.create_update_playlist

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.view_model.library.playlist.NewPlaylistViewModel
import com.example.playlistmaker.ui.utils.GlideImageLoader.loadPlaylistCornerImage
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UpdatePlaylistInfoFragment : BasePlaylistCreator() {

    private val args by navArgs<UpdatePlaylistInfoFragmentArgs>()

    override val viewModel: NewPlaylistViewModel by viewModel { parametersOf(args.playlist) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            with(args.playlist) {
                etName.setText(name)
                etDescription.setText(description)
                binding.btnAddPlaylistImg.scaleType = ImageView.ScaleType.CENTER_CROP
                loadPlaylistCornerImage(image, requireContext(), btnAddPlaylistImg)
            }
            btnBack.text = requireContext().getString(R.string.edit_playlist)
            btnCreate.text = requireContext().getString(R.string.save)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if (state.first) findNavController().popBackStack()
            }
        }
    }

    override fun showExitDialog() {
        findNavController().popBackStack()
    }

    override fun onFinishButtonClicked() {
        val name = binding.etName.text.toString()
        val description = binding.etDescription.text.toString()
        if (name.isNotBlank()) {
            viewModel.updatePlaylist(name, description, selectedImageUri)
        }
    }
}