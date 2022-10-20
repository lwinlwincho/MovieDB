package com.llc.moviebd.data.data_result

import com.llc.moviebd.data.model.MovieDetailModel

sealed class MovieDetailEvent {
    data class Success(val movieDetailModel: MovieDetailModel) : MovieDetailEvent()
    data class Error(val error: String) : MovieDetailEvent()
    object Loading : MovieDetailEvent()
}

