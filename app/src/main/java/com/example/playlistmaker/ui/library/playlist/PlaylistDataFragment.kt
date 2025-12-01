package com.example.playlistmaker.ui.library.playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDataBinding
import com.example.playlistmaker.domain.favourite_songs.model.Playlist
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.view_model.library.playlist.PlaylistDataViewModel
import com.example.playlistmaker.ui.FragmentBinding
import com.example.playlistmaker.ui.library.playlist.adapter.PlaylistSongsAdapter
import com.example.playlistmaker.ui.utils.GlideImageLoader.loadPlaylistCornerImage
import com.example.playlistmaker.ui.utils.GlideImageLoader.loadPlaylistImage
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlaylistDataFragment : FragmentBinding<FragmentPlaylistDataBinding>() {

    private val args by navArgs<PlaylistDataFragmentArgs>()

    private val playlist by lazy { args.playlist }

    private val viewModel: PlaylistDataViewModel by viewModel { parametersOf(playlist) }

    private val playlistDataAdapter = PlaylistSongsAdapter(
        onItemClick = { song -> navigateToPlayer(song) },
        onItemLongClick = { songId -> showDeleteSongDialog(songId) })

    override fun createBinding(layoutInflater: LayoutInflater, container: ViewGroup?) =
        FragmentPlaylistDataBinding.inflate(layoutInflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val bottomSheet = BottomSheetBehavior.from(binding.bottomSheet)
        binding.playlistMainLayout.doOnLayout {
            bottomSheet.peekHeight = getBottomSheetHeight()
        }

        binding.rvSongs.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = playlistDataAdapter
        }

        val bottomSheetMore = BottomSheetBehavior.from(binding.bottomSheetMore).apply {
            state = STATE_HIDDEN

            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = (slideOffset + 1f) / 2
                }
            })
        }

        binding.apply {
            btnMore.setOnClickListener {
                bottomSheetMore.state = STATE_COLLAPSED
            }

            btnShare.setOnClickListener {
                sharePlaylist()
            }

            btnMoreShare.setOnClickListener {
                bottomSheetMore.state = STATE_HIDDEN
                sharePlaylist()
            }

            btnMoreDelete.setOnClickListener {
                bottomSheetMore.state = STATE_HIDDEN
                showDeletePlaylistDialog(playlist.id!!)
            }

            btnMoreEdit.setOnClickListener {
                findNavController().navigate(
                    PlaylistDataFragmentDirections.actionPlaylistDataFragmentToEditPlaylistFragment(
                        viewModel.playlistMainInfo.value
                    )
                )
            }
        }

        viewModel.loadPlaylistInfo(playlist.id!!)

        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                viewModel.playlistMainInfo.collect { playlist ->
                    updatePlaylistInfo(playlist)
                }
            }

            launch {
                viewModel.playlistTime.collect { minutes ->
                    binding.tvPlaylistTime.text = requireContext().resources.getQuantityString(
                        R.plurals.minute_plurals, minutes, minutes
                    )
                }
            }
            launch {
                viewModel.songs.collect { songs ->
                    playlistDataAdapter.apply {
                        this.songs = songs
                        notifyDataSetChanged()
                    }
                }
            }
            launch {
                viewModel.deletePlaylistState.collect { state ->
                    if (state) findNavController().popBackStack()
                }
            }
        }
    }

    private fun navigateToPlayer(song: Song) {
        findNavController().navigate(
            PlaylistDataFragmentDirections.actionPlaylistDataFragmentToPlayerFragment(
                song
            )
        )
    }

    private fun showDeleteSongDialog(songId: String) {
        val message = requireContext().getString(R.string.dialog_delete_song_from_playlist_title)
        showDialog(message) {
            viewModel.deleteSongFromPlaylist(playlist.id!!, songId)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun sharePlaylist() {
        if (viewModel.songs.value.isEmpty()) {
            val message = requireContext().getString(R.string.no_songs_in_playlist)

            val parent = requireActivity().findViewById<ViewGroup>(R.id.mainContainerView)

            val snackBar = Snackbar.make(parent, "", Snackbar.LENGTH_SHORT)

            val snackbarView = LayoutInflater.from(requireActivity()).inflate(
                R.layout.snackbar_new_playlist,
                parent,
                false
            )
            snackbarView.findViewById<TextView>(R.id.tvText).text = message
            val snackbarLayout = snackBar.view as Snackbar.SnackbarLayout
            snackbarLayout.setPadding(0, 0, 0, 0)
            snackbarLayout.addView(snackbarView)
            snackBar.show()

            return
        }
        viewModel.sharePlaylist(
            getSongsCountEnding(),
            requireContext().getString(R.string.playlist_sharing_song_format)
        )
    }

    private fun showDeletePlaylistDialog(playlistId: Int) {
        val message = requireContext().getString(R.string.dialog_delete_playlist_title)
        val formattedMessage = String.format(message, viewModel.playlistMainInfo.value.name)
        showDialog(formattedMessage) {
            viewModel.deletePlaylist(playlistId)
        }
    }

    private fun showDialog(message: String, onConfirm: () -> Unit) {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.MyCustomDialogTheme)
            .setMessage(message)
            .setNegativeButton(requireContext().getString(R.string.no)) { dialog, which -> dialog.cancel() }
            .setPositiveButton(requireContext().getString(R.string.yes)) { dialog, which ->
                onConfirm()
                dialog.dismiss()
            }
            .show()
    }

    private fun updatePlaylistInfo(playlist: Playlist) {
        binding.apply {
            playlist.apply {
                loadPlaylistImage(playlist.image, requireContext(), binding.imgPlaylist)
                tvPlaylistName.text = name
                tvPlaylistSongsCount.text =
                    requireContext().resources.getQuantityString(
                        R.plurals.tracks_plurals, songsCount, songsCount
                    )
                tvPlaylistDescription.text = description
                bsTVPlaylistName.text = tvPlaylistName.text
                bsTVSongsCount.text = tvPlaylistSongsCount.text
                loadPlaylistCornerImage(playlist.image, requireContext(), binding.bsImgPlaylist)
            }
        }
    }

    private fun getSongsCountEnding(): String {
        val count = viewModel.songs.value.size
        return requireContext().resources.getQuantityString(
            R.plurals.tracks_plurals,
            count,
            count
        )
    }

    private fun getBottomSheetHeight(): Int {
        val coordinatorHeight = binding.coordinatorLayout.height
        val mainHeight = binding.playlistMainLayout.height
        val peekHeight = coordinatorHeight - mainHeight
        return peekHeight
    }
}