package com.ccc.ncs.network.converter

import com.ccc.ncs.model.Artist
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
        }

        return null
    }
}