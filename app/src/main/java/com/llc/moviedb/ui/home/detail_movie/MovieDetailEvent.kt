package com.llc.moviedb.ui.home.detail_movie

import com.llc.moviedb.data.model.CreditModel
import com.llc.moviedb.data.model.MovieDetailModel

sealed class MovieDetailEvent {
    object Loading : MovieDetailEvent()
    data class Success(val movieDetailModel: MovieDetailModel) : MovieDetailEvent()
    data class Credits(val creditModel: CreditModel) : MovieDetailEvent()
    data class SuccessRemoved(val message: String) : MovieDetailEvent()
    data class SuccessAdded(val message: String) : MovieDetailEvent()
    data class Error(val error: String) : MovieDetailEvent()
}

