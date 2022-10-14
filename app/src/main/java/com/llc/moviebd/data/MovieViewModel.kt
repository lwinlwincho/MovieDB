package com.llc.moviebd.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MovieViewModel:ViewModel() {

    private var _uiEvent = MutableLiveData<List<MovieModel>>()
    val uiEvent: LiveData<List<MovieModel>> get() = _uiEvent

    init {
         val movieList: List<MovieModel> = MovieData().loadMovieData()
        //add(data) movie list in live data
        _uiEvent.postValue(movieList)
    }

}