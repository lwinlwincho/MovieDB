package com.llc.moviedb.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favouriteMovieEntity")
data class FavouriteMovieEntity(
    @PrimaryKey
    val id: Long = 0,
    @ColumnInfo(name = "poster")
    val posterPath: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "date")
    val releaseDate: String,
    @ColumnInfo(name = "vote")
    val voteAverage: String
)