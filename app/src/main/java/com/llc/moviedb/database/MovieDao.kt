package com.llc.moviedb.database

import androidx.room.*

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavouriteMovie(item: FavouriteMovieEntity)

    @Delete
    suspend fun delete(item: FavouriteMovieEntity)

    @Query("SELECT * from favouriteMovieEntity WHERE id = :id")
    fun getFavouriteMovieById(id: Long): FavouriteMovieEntity?

    @Query("SELECT * from favouriteMovieEntity ORDER BY id ASC")
    fun getAllFavouriteMovie(): List<FavouriteMovieEntity>
}