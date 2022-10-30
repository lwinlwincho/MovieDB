package com.llc.moviebd.ui.home.detail_movie

import com.llc.moviebd.data.model.CreditModel
import com.llc.moviebd.data.model.MovieDetailModel

sealed class MovieDetailEvent {
    object Loading : MovieDetailEvent()
    data class Success(val movieDetailModel: MovieDetailModel) : MovieDetailEvent()
    data class Credits(val creditModel: CreditModel) : MovieDetailEvent()
    data class Error(val error: String) : MovieDetailEvent()

}

