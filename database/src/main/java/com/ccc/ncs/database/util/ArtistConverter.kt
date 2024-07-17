package com.ccc.ncs.database.util

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.ccc.ncs.model.Artist
import com.google.gson.Gson

@ProvidedTypeConverter
internal class ArtistConverter(
    private val gson: Gson
) {
    @TypeConverter
    fun stringToArtistList(value: String?): List<Artist> = gson.fromJson(value, Array<Artist>::class.java)?.toList() ?: emptyList()

    @TypeConverter
    fun artistListToString(value: List<Artist>): String = gson.toJson(value)
}