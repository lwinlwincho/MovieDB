package com.llc.moviedb.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeMovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

/*
    private val _nowShowingUiEvent = MutableLiveData<MovieUpcomingEvent>()
    val nowShowingUiEvent: LiveData<MovieUpcomingEvent> = _nowShowingUiEvent

    private val _popularUiEvent = MutableLiveData<MovieUpcomingEvent>()
    val popularUiEvent: LiveData<MovieUpcomingEvent> = _popularUiEvent*/

    private val _nowShowingUiEvent =
        MutableStateFlow<MovieUpcomingEvent>(MovieUpcomingEvent.Loading)
    val nowShowingUiEvent: StateFlow<MovieUpcomingEvent> = _nowShowingUiEvent

    private val _popularUiEvent =
        MutableStateFlow<MovieUpcomingEvent>(MovieUpcomingEvent.Loading)
    val popularUiEvent: StateFlow<MovieUpcomingEvent> = _popularUiEvent

    init {
        getNowShowing()
        getPopular()
    }

    private fun getNowShowing() {

        _nowShowingUiEvent.value = MovieUpcomingEvent.Loading

        viewModelScope.launch {

            //get data from repository
/*
            try {
                val result =
                    movieRepository.getNowShowingMovies().results.sortedByDescending { it.releaseDate }
                _nowShowingUiEvent.value = MovieUpcomingEvent.Success(result)
            }
            catch (e: Exception) {
                _nowShowingUiEvent.value = MovieUpcomingEvent.Failure(e.message.toString())
            }
*/

            movieRepository.getNowShowingMovies()
                .catch { e ->
                    _nowShowingUiEvent.value = MovieUpcomingEvent.Failure(e.message.toString())
                }
                .collectLatest {
                    _nowShowingUiEvent.value = MovieUpcomingEvent.Success(it.results)
                }
        }
    }

    private fun getPopular() {
        _popularUiEvent.value = MovieUpcomingEvent.Loading

        viewModelScope.launch {
            /*try {
                //get data from repository
                val popularResult =
                    movieRepository.getPopularMovies().results.sortedByDescending { it.vote_average }
                _popularUiEvent.value = MovieUpcomingEvent.Success(popularResult)

            } catch (e: Exception) {
                _popularUiEvent.value = MovieUpcomingEvent.Failure(e.message.toString())
            }*/

            movieRepository.getPopularMovies()
                .catch { e ->
                    _popularUiEvent.value = MovieUpcomingEvent.Failure(e.message.toString())
                }
                .collectLatest {
                    _popularUiEvent.value = MovieUpcomingEvent.Success(it.results)
                }
        }
    }
}

//You can store many data class and singleton obj in sealed class
sealed class MovieUpcomingEvent {
    data class Success(val movieList: List<MovieModel>) : MovieUpcomingEvent()
    data class Failure(val message: String) : MovieUpcomingEvent()
    object Loading : MovieUpcomingEvent()
}