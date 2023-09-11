package com.llc.moviedb.di

import com.llc.moviedb.network.MovieAPIService
import com.llc.moviedb.repository.MovieRepository
import com.llc.moviedb.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/*
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(movieApiService: MovieAPIService): MovieRepository {
        return MovieRepositoryImpl(movieApiService)
    }
}
*/

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepositoryImpl(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository
}

