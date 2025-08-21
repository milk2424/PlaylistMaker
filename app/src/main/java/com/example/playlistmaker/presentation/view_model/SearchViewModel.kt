package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.search.interactor.SongsInteractor
import com.example.playlistmaker.domain.search.model.ResponseStatus
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.utils.search.SongState
import com.example.playlistmaker.presentation.utils.search.SongState.Empty
import com.example.playlistmaker.presentation.utils.search.SongState.History
import com.example.playlistmaker.presentation.utils.search.SongState.Loading
import com.example.playlistmaker.presentation.utils.search.SongState.NetworkError
import com.example.playlistmaker.presentation.utils.search.SongState.Successful

class SearchViewModel(private val songsInteractor: SongsInteractor) : ViewModel() {
    companion object {
        fun getFactory(): ViewModelProvider.Factory {
            val songsInteractor = Creator.provideSongsInteractor()
            return viewModelFactory {
                initializer { SearchViewModel(songsInteractor) }
            }
        }
    }

    private val songStateMutableLiveData = MutableLiveData<SongState>()
    fun songStateLiveData() = songStateMutableLiveData

    init {
        loadHistory()
    }

    fun clearHistory() = songsInteractor.clearSongHistory()

    fun addSongToHistory(song: Song) = songsInteractor.addSongToHistory(song)

    fun loadHistory() {
        songStateMutableLiveData.value = Loading
        songStateMutableLiveData.postValue(History(songsInteractor.loadSongHistory().reversed()))
    }

    fun loadSongsFromApi(songName: String) {
        songStateMutableLiveData.value = Loading
        songsInteractor.loadSongsFromApi(songName = songName,
            object : SongsInteractor.TracksConsumer {
                override fun consume(response: ResponseStatus) {
                    when (response) {
                        is ResponseStatus.Empty -> songStateMutableLiveData.postValue(Empty)
                        is ResponseStatus.Error -> songStateMutableLiveData.postValue(NetworkError)
                        is ResponseStatus.Successful -> songStateMutableLiveData.postValue(
                            Successful(response.songs)
                        )
                    }
                }
            })
    }
}