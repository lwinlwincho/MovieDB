package com.llc.moviedb

sealed class Resource<T>(val data: T? = null, val throwable: Throwable? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(throwable: Throwable, data: T? = null) : Resource<T>(data, throwable)
}