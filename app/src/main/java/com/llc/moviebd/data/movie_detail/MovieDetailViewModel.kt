package com.llc.moviebd.data.movie_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.llc.moviebd.network.MoviePhoto

class MovieDetailViewModel : ViewModel() {

    private val _detailEvent = MutableLiveData<MoviePhoto>()
    val detailEvent: LiveData<MoviePhoto> = _detailEvent




}