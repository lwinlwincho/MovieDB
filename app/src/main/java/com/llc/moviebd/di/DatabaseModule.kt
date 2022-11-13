package com.llc.moviebd.di

import android.content.Context
import androidx.room.Room
import com.llc.moviebd.database.MovieDao
import com.llc.moviebd.database.MovieRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DatabaseModule {

    @Provides
    @ViewModelScoped
    fun provideMovieRoomDatabase(@ApplicationContext context: Context): MovieRoomDatabase {
        return Room.databaseBuilder(
            context,
            MovieRoomDatabase::class.java,
            "movie_database"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @ViewModelScoped
    fun provideMovieDao(movieRoomDatabase: MovieRoomDatabase): MovieDao {
        return movieRoomDatabase.movieDao()
    }
}