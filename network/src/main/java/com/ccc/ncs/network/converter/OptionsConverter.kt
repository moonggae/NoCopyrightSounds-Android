package com.ccc.ncs.network.converter

import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Converter

abstract class OptionsConverter<T, T2> : Converter<ResponseBody, Pair<List<T>, List<T2>>> {
    abstract override fun convert(value: ResponseBody): Pair<List<T>, List<T2>>?

    protected fun parseGenres(document: Document): List<Genre> {
        val options = document.selectFirst("select[name=\"genre\"]")?.select("option") ?: emptyList()
        return options.map {
            Genre(
                id = it.attr("value").toIntOrNull() ?: return@map null,
                name = it.text(),
                null,
                null
            )
        }.filterNotNull()
    }

    protected fun parseMoods(document: Document): List<Mood> {
        val options = document.selectFirst("select[name=\"mood\"]")?.select("option") ?: emptyList()
        return options.map {
            Mood(
                id = it.attr("value").toIntOrNull() ?: return@map null,
                name = it.text(),
                null,
                null
            )
        }.filterNotNull()
    }
}

class GenreAndMoodConverter : OptionsConverter<Genre, Mood>() {
    override fun convert(value: ResponseBody): Pair<List<Genre>, List<Mood>>? {
        val document = Jsoup.parse(value.string())
        val moods: List<Mood> = parseMoods(document)
        val genres: List<Genre> = parseGenres(document)
        return Pair(genres, moods)
    }
}

class MoodAndGenreConverter: OptionsConverter<Mood, Genre>() {
    override fun convert(value: ResponseBody): Pair<List<Mood>, List<Genre>>? {
        val document = Jsoup.parse(value.string())
        val moods: List<Mood> = parseMoods(document)
        val genres: List<Genre> = parseGenres(document)
        return Pair(moods, genres)
    }
}