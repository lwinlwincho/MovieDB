package com.llc.moviedb.datastore

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object MovieListSerializer : Serializer<MovieList> {
    override val defaultValue: MovieList
        get() = MovieList()

    override suspend fun readFrom(input: InputStream): MovieList {
        return try {
            Json.decodeFromString(
                deserializer = MovieList.serializer(),
                string = input.readBytes().decodeToString()
            )
        }catch (e : SerializationException){
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: MovieList, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = MovieList.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}