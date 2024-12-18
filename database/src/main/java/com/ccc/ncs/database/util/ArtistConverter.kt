package com.ccc.ncs.database.util

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.ccc.ncs.model.Artist
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser


@ProvidedTypeConverter
internal class ArtistConverter(
    private val gson: Gson
) {
    @TypeConverter
    fun stringToArtistList(value: String?): List<Artist> {
        if (value.isNullOrEmpty()) return emptyList()

        return try {
            parseUsingGson(value)
        } catch (e: Exception) {
            parseUsingJsonParser(value)
        }
    }

    private fun parseUsingGson(value: String): List<Artist> {
        return gson.fromJson(value, Array<Artist>::class.java)
            ?.toList()
            ?.takeIf { artists -> artists.all { isValidArtist(it) } }
            ?: throw IllegalStateException("Failed to convert using Gson")
    }

    private fun parseUsingJsonParser(value: String): List<Artist> {
        return try {
            JsonParser.parseString(value)
                .asJsonArray
                .mapNotNull { element -> parseArtistFromJson(element.asJsonObject) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun parseArtistFromJson(jsonObject: JsonObject): Artist? {
        return try {
            val entries = jsonObject.entrySet().toList()
            Artist(
                name = entries.getOrNull(0)?.value?.asString ?: throw IllegalStateException("No artist name"),
                detailUrl = entries.getOrNull(1)?.value?.asString ?: "",
                photoUrl = entries.getOrNull(2)?.value?.asString,
                tags = entries.getOrNull(3)?.value?.asString ?: ""
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @Suppress("SENSELESS_COMPARISON")
    private fun isValidArtist(artist: Artist): Boolean =
        artist.name != null && artist.detailUrl != null && artist.tags != null

    @TypeConverter
    fun artistListToString(value: List<Artist>): String = gson.toJson(value)
}