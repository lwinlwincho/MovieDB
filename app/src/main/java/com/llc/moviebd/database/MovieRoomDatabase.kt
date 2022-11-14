package com.llc.moviebd.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavouriteMovieEntity::class], version = 2)
abstract class MovieRoomDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}