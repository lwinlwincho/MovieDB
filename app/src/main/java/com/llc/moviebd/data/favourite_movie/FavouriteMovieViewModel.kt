package com.llc.moviebd.data.favourite_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.llc.moviebd.data.model.MovieModel

class FavouriteMovieViewModel :ViewModel(){

    private var _favMovie=MutableLiveData<List<MovieModel>>()
    val favEvent:LiveData<List<MovieModel>> get()=_favMovie
}