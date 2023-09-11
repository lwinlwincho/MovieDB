package com.llc.moviedb.di

import com.llc.moviedb.dataa.remoteDataSource.localDataSource.LocalDataSource
import com.llc.moviedb.dataa.remoteDataSource.localDataSource.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {
    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource
}