package com.example.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.interactor.SongsInteractor
import com.example.playlistmaker.domain.search.model.ResponseStatus
import com.example.playlistmaker.domain.search.model.Song
import com.example.playlistmaker.presentation.utils.search.SongState
import com.example.playlistmaker.presentation.utils.search.SongState.Empty
import com.example.playlistmaker.presentation.utils.search.SongState.History
import com.example.playlistmaker.presentation.utils.search.SongState.Loading
import com.example.playlistmaker.presentation.utils.search.SongState.NetworkError
import com.example.playlistmaker.presentation.utils.search.SongState.Successful
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val songsInteractor: SongsInteractor) : ViewModel() {
    private val songStateMutableLiveData = MutableLiveData<SongState>()

    private var loadSongsJob: Job? = null

    fun songStateLiveData(): LiveData<SongState> = songStateMutableLiveData

    init {
        loadHistory()
    }

    fun clearHistory() = songsInteractor.clearSongHistory()

    fun addSongToHistory(song: Song) = songsInteractor.addSongToHistory(song)

    fun loadHistory() {
        songStateMutableLiveData.value = Loading
        songStateMutableLiveData.postValue(History(songsInteractor.loadSongHistory().reversed()))
    }

    fun loadSongsFromApi(songName: String, searchDelay: Long) {
        loadSongsJob?.cancel()
        songStateMutableLiveData.postValue(Loading)
        loadSongsJob = viewModelScope.launch(Dispatchers.IO) {
            delay(searchDelay)
            songsInteractor
                .loadSongsFromApi(songName = songName)
                .collect { response ->
                    when (response) {
                        is ResponseStatus.Empty -> songStateMutableLiveData.postValue(Empty)
                        is ResponseStatus.Error -> songStateMutableLiveData.postValue(
                            NetworkError
                        )

                        is ResponseStatus.Successful -> songStateMutableLiveData.postValue(
                            Successful(response.songs)
                        )
                    }
                }
        }
    }

}