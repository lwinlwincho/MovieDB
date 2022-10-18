package com.llc.moviebd.data.movie_poster

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.llc.moviebd.data.MovieData
import com.llc.moviebd.data.MovieModel

class MovieListViewModel:ViewModel() {

    private var _uiEvent = MutableLiveData<List<MovieModel>>()
    val uiEvent: LiveData<List<MovieModel>> get() = _uiEvent

    init {
         val movieList: List<MovieModel> = MovieData().listMoviePoster()
        //add(data) movie list into live data
        _uiEvent.postValue(movieList)

    }

}