package com.ccc.ncs.network.converter

import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.ArtistDetail
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
        return when {
            getRawType(type) == List::class.java -> {
                val innerType = (type as ParameterizedType).actualTypeArguments[0]
                when (innerType) {
                    Music::class.java -> MusicListConverter()
                    Artist::class.java -> ArtistListConverter()
                    else -> null
                }
            }
            getRawType(type) == Pair::class.java -> {
                val parameterizedType = type as ParameterizedType
                val firstPairType = parameterizedType.actualTypeArguments[0] as ParameterizedType
                val secondPairType = parameterizedType.actualTypeArguments[1] as ParameterizedType
                val firstInnerType = firstPairType.actualTypeArguments[0]
                val secondInnerType = secondPairType.actualTypeArguments[0]
                when {
                    firstInnerType == Genre::class.java && secondInnerType == Mood::class.java -> GenreAndMoodConverter()
                    firstInnerType == Mood::class.java && secondInnerType == Genre::class.java -> MoodAndGenreConverter()
                    else -> null
                }
            }
            type == ArtistDetail::class.java -> ArtistDetailConverter()
            else -> null
        }
    }
}