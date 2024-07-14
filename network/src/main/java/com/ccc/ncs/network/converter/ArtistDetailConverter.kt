package com.ccc.ncs.network.converter

import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.ArtistDetail
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Converter
import java.net.URI

class ArtistDetailConverter: Converter<ResponseBody, ArtistDetail?> {
    override fun convert(responseBody: ResponseBody): ArtistDetail? {
        try {
            val document = Jsoup.parse(responseBody.string())
            return parseArtistDetail(document)
        }
        catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun parseArtistDetail(document: Document): ArtistDetail {
        val artist = parseArtistInformation(document)
        val musics = MusicListConverter().parseMusicList(document)
        val similarArtists = ArtistListConverter().parseArtists(document)

        return ArtistDetail(
            artist = artist,
            musics = musics,
            similarArtists = similarArtists
        )
    }

    private fun parseArtistInformation(document: Document): Artist {
        val url = document
            .head()
            .select("meta[property=og:url]")
            .attr("content")
        val path = URI.create(url).path

        val name = document.select("body article.module.details div.info h5").text()
        val tags = document.select("body article.module.details div.info span.tags").text()

        val imageStyleString = document.select("body article.module.details div.img").attr("style")
        val photoUrl = imageStyleString.split("'")[1]

        return Artist(
            name = name,
            photoUrl = photoUrl,
            detailUrl = path,
            tags = tags
        )
    }
}