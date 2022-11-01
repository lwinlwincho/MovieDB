package com.llc.myinventory.database

import android.app.Application

class MovieApplication : Application() {
    val database: MovieRoomDatabase by lazy {
        MovieRoomDatabase.getDatabase(this)
    }
}