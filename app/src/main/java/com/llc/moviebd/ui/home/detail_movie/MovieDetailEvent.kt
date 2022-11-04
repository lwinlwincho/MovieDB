package com.llc.moviebd.ui.home.detail_movie

import com.llc.moviebd.data.model.CreditModel
import com.llc.moviebd.data.model.MovieDetailModel
import com.llc.moviebd.database.MovieEntity
import com.llc.moviebd.favourite_movie.FavouriteEvent
import com.llc.moviebd.ui.home.MovieUpcomingEvent

sealed class MovieDetailEvent {
    object Loading : MovieDetailEvent()
    data class Success(val movieDetailModel: MovieDetailModel) : MovieDetailEvent()
    data class SuccessRemoved(val message: String) : MovieDetailEvent()
    data class SuccessAdded(val message: String) : MovieDetailEvent()
    data class Credits(val creditModel: CreditModel) : MovieDetailEvent()
    data class Error(val error: String) : MovieDetailEvent()

}

