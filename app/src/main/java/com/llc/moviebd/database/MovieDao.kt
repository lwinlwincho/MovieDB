package com.llc.myinventory.database

import androidx.room.*
import com.llc.moviebd.database.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: MovieEntity)

    @Delete
    suspend fun delete(item: MovieEntity)

    @Query("SELECT * from movieentity WHERE id = :id")
    fun getById(id: Int): MovieEntity

    @Query("SELECT * from movieentity ORDER BY id ASC")
    fun getAllFavMovie(): List<MovieEntity>
}