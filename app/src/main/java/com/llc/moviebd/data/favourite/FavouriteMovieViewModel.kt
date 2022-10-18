package com.llc.moviebd.data.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.llc.moviebd.data.MovieModel

class FavouriteMovieViewModel :ViewModel(){

    private var _favMovie=MutableLiveData<List<MovieModel>>()
    val favEvent:LiveData<List<MovieModel>> get()=_favMovie
}