package com.ccc.ncs.network.converter

import com.ccc.ncs.model.Artist
import com.ccc.ncs.network.BuildConfig
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Converter

private const val WEB_URL = BuildConfig.WEB_URL

class ArtistListConverter: Converter<ResponseBody, List<Artist>> {
    override fun convert(responseBody: ResponseBody): List<Artist> {
        val document = Jsoup.parse(responseBody.string())
        return parseArtists(document)
    }

    fun parseArtists(document: Document): List<Artist> {
        val artistElements = findArtistDivs(document)

        return artistElements.map { element ->
            val detailUrl = element.select("a").attr("href")
            val name = element.select("div.bottom strong").text()
            val imageStyleString = element.select("div.img").attr("style")
            val photoUrl = imageStyleString.split("'")[1]
            val tags = element.select("span.tags").text()

            Artist(
                name = name,
                detailUrl = detailUrl,
                photoUrl = if (photoUrl.startsWith("/static")) WEB_URL + photoUrl else photoUrl,
                tags = tags
            )
        }
    }

    private fun findArtistDivs(doc: Document): Elements {
        return doc.select("body > main > article.module.artists div.row > div.item")
    }
}