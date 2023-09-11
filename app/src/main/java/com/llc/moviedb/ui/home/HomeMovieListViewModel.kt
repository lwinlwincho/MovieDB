package com.llc.moviedb.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.llc.moviedb.data.model.MovieModel
import com.llc.moviedb.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeMovieListViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    /*
        private val _nowShowingUiEvent = MutableLiveData<MovieUpcomingEvent>()
        val nowShowingUiEvent: LiveData<MovieUpcomingEvent> = _nowShowingUiEvent

        private val _popularUiEvent = MutableLiveData<MovieUpcomingEvent>()
        val popularUiEvent: LiveData<MovieUpcomingEvent> = _popularUiEvent*/

    /* private val _nowShowingUiState =
         MutableStateFlow<MovieUpcomingUiState>(MovieUpcomingUiState.Loading)
     val nowShowingUiStated: StateFlow<MovieUpcomingUiState> = _nowShowingUiState*/

    val uiState = combine(
        movieRepository.nowShowingMoviesFlow.map { it.results },
        movieRepository.popularMoviesFlow.map { it.results }
    ) { nowShowingMovies, popularMovies ->
        MovieUpcomingUiState.Success(
            nowShowingMovies = nowShowingMovies,
            popularMovies = popularMovies
        )
    }.catch { e ->
        MovieUpcomingUiState.Failure(e.message.toString())
    }.stateIn(
        scope = viewModelScope,
        initialValue = MovieUpcomingUiState.Loading,
        started = SharingStarted.WhileSubscribed(5000L)
    )

    /*private val _popularUiEvent =
        MutableStateFlow<MovieUpcomingUiState>(MovieUpcomingUiState.Loading)
    val popularUiEvent: StateFlow<MovieUpcomingUiState> = _popularUiEvent*/

    init {
        //  getNowShowing()
        // getPopular()
    }

    /*private fun getNowShowing() {

        _nowShowingUiState.value = MovieUpcomingUiState.Loading

        viewModelScope.launch {

            //get data from repository
            *//*
                        try {
                            val result =
                                movieRepository.getNowShowingMovies().results.sortedByDescending { it.releaseDate }
                            _nowShowingUiEvent.value = MovieUpcomingEvent.Success(result)
                        }
                        catch (e: Exception) {
                            _nowShowingUiEvent.value = MovieUpcomingEvent.Failure(e.message.toString())
                        }
            *//*

            movieRepository.getNowShowingMovies()
                .catch { e ->
                    _nowShowingUiState.value = MovieUpcomingUiState.Failure(e.message.toString())
                }
                .collectLatest {
                    _nowShowingUiState.value = MovieUpcomingUiState.Success(it.results)
                }
        }
    }*/

    /*private fun getPopular() {
        _popularUiEvent.value = MovieUpcomingUiState.Loading

        viewModelScope.launch {
            *//*try {
                //get data from repository
                val popularResult =
                    movieRepository.getPopularMovies().results.sortedByDescending { it.vote_average }
                _popularUiEvent.value = MovieUpcomingEvent.Success(popularResult)

            } catch (e: Exception) {
                _popularUiEvent.value = MovieUpcomingEvent.Failure(e.message.toString())
            }*//*

            movieRepository.getPopularMovies()
                .catch { e ->
                    _popularUiEvent.value = MovieUpcomingUiState.Failure(e.message.toString())
                }
                .collectLatest {
                    _popularUiEvent.value = MovieUpcomingUiState.Success(it.results)
                }
        }
    }*/
}

//You can store many data class and singleton obj in sealed class
sealed class MovieUpcomingUiState {
    data class Success(
        val nowShowingMovies: List<MovieModel>,
        val popularMovies: List<MovieModel>
    ) : MovieUpcomingUiState()

    data class Failure(val message: String) : MovieUpcomingUiState()
    object Loading : MovieUpcomingUiState()
}