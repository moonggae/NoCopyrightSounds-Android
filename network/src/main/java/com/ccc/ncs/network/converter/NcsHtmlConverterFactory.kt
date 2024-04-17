package com.ccc.ncs.network.converter

import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NcsHtmlConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        if (getRawType(type) == List::class.java) {
            val innerType = (type as ParameterizedType).actualTypeArguments[0]
            return when (innerType) {
                Music::class.java -> MusicListConverter()
                Artist::class.java -> ArtistListConverter()
                else -> null
            }
        } else if (getRawType(type) == Pair::class.java) {
            val firstPairType = (type as ParameterizedType).actualTypeArguments[0]
            val secondPairType = (type as ParameterizedType).actualTypeArguments[1]
            val firstInnerType = (firstPairType as ParameterizedType).actualTypeArguments[0]
            val secondInnerType = (secondPairType as ParameterizedType).actualTypeArguments[0]
            return when {
                firstInnerType == Genre::class.java && secondInnerType == Mood::class.java -> GenreAndMoodConverter()
                firstInnerType == Mood::class.java && secondInnerType == Genre::class.java -> MoodAndGenreConverter()
                else -> null
            }
        }

        return null
    }
}