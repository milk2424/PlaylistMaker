package com.example.playlistmaker.ui.library.playlist.create_update_playlist

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.presentation.mapper.player_mapper.DpToPxMapper
import com.example.playlistmaker.presentation.view_model.library.playlist.NewPlaylistViewModel
import com.example.playlistmaker.ui.FragmentBinding

abstract class BasePlaylistCreator : FragmentBinding<FragmentNewPlaylistBinding>() {

    var selectedImageUri: Uri? = null

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

    abstract val viewModel: NewPlaylistViewModel

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentNewPlaylistBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddPlaylistImg.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.etName.doOnTextChanged { text, p1, p2, p3 ->
            binding.btnCreate.isEnabled = text?.isNotBlank() ?: false
        }


        binding.btnCreate.setOnClickListener {
            onFinishButtonClicked()
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
    }

    protected fun canUserGoBack() {
        if (!binding.etName.text.isNullOrBlank() || !binding.etDescription.text.isNullOrBlank() || selectedImageUri != null) showExitDialog()
        else findNavController().popBackStack()
    }

    protected abstract fun showExitDialog()

    protected abstract fun onFinishButtonClicked()
}