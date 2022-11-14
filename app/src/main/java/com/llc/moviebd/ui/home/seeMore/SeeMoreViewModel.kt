package com.llc.moviebd.ui.home.seeMore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviebd.data.model.MovieModel
import com.llc.moviebd.network.MovieAPIService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SeeMoreViewModel @Inject constructor(
    private val movieAPIService: MovieAPIService
) : ViewModel() {

    private val _seeMoreUiEvent = MutableLiveData<SeeMoreEvent>()
    val seeMoreUiEvent: LiveData<SeeMoreEvent> = _seeMoreUiEvent

    fun getNowShowing() {
        _seeMoreUiEvent.value = SeeMoreEvent.Loading
        viewModelScope.launch {
            try {
                //get data from web server
                val result =
                    movieAPIService.getNowPlaying().results.sortedByDescending { it.releaseDate }
                _seeMoreUiEvent.value = SeeMoreEvent.Success(result)
            } catch (e: Exception) {
                _seeMoreUiEvent.value = SeeMoreEvent.Failure(e.message.toString())
            }
        }
    }

    fun getPopular() {
        _seeMoreUiEvent.value = SeeMoreEvent.Loading
        viewModelScope.launch {
            try {
                //get data from web server

                val popularResult =
                    movieAPIService.getPopular().results.sortedByDescending { it.vote_average }
                _seeMoreUiEvent.value = SeeMoreEvent.Success(popularResult)

            } catch (e: Exception) {
                _seeMoreUiEvent.value = SeeMoreEvent.Failure(e.message.toString())
            }
        }
    }
}

sealed class SeeMoreEvent {
    data class Success(val movieList: List<MovieModel>) : SeeMoreEvent()
    data class Failure(val message: String) : SeeMoreEvent()
    object Loading : SeeMoreEvent()
}