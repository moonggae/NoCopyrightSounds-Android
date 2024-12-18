package com.ccc.ncs.network.converter

import com.ccc.ncs.model.Artist
import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.Version
import com.ccc.ncs.network.BuildConfig
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import retrofit2.Converter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.util.UUID

private const val WEB_URL = BuildConfig.WEB_URL

class MusicListConverter: Converter<ResponseBody, List<Music>> {
    override fun convert(resonseBody: ResponseBody): List<Music> {
        val document = Jsoup.parse(resonseBody.string())
        return parseMusicList(document)
    }

    private fun findMusicTable(doc: Document): Element? {
        return doc.select("body > main > article.module.table").firstOrNull { element ->
            !element.classNames().contains("featured")
        }
    }

    fun parseMusicList(document: Document): List<Music> {
        val musicTable = findMusicTable(document)
        return musicTable?.select("tbody > tr")?.mapNotNull { row ->
            try {
                parseMusicRow(row)
            } catch (e: Throwable) {
                e.printStackTrace()
                null
            }
        }?: emptyList()
    }

    private fun parseMusicRow(row: Element): Music {
        val titleTag = row.selectFirst("td:nth-child(1) > a") ?: throw IllegalArgumentException("Not Found title")
        val artists = parseArtists(titleTag)
        val releaseDate = parseReleaseDate(row) ?: throw IllegalArgumentException("Not Found ReleaseDate")
        val genres = parseGenres(row)
        val moods = parseMoods(row)
        val versions = parseVersions(row)
        val dataUrl = titleTag.attribute("data-url").value
        val id = UUID.fromString(titleTag.attribute("data-tid").value)

        if (dataUrl.isNullOrBlank()) throw IllegalArgumentException("Not Found dataUrl")

        return Music(
            id = id,
            title = titleTag.attribute("data-track").value,
            artists = artists,
            dataUrl = dataUrl,
            coverThumbnailUrl = titleTag.attribute("data-cover").value,
            coverUrl = titleTag.attribute("data-cover").value.replace("100x100", "1000x0"),
            detailUrl = WEB_URL + (row.selectFirst("td:nth-child(3) > a")?.attr("href") ?: ""),
            releaseDate = releaseDate,
            genres = genres.toSet(),
            moods = moods.toSet(),
            versions = versions.toSet()
        )
    }

    private fun parseArtists(tag: Element): List<Artist> {
        val artistTagHtml = tag.attribute("data-artist").html()
        return Jsoup.parse(artistTagHtml).select("a").map { aTag ->
            Artist(
                name = aTag.text(),
                detailUrl = aTag.attribute("href").value,
                photoUrl = null,
                tags = ""
            )
        }
    }

    private fun parseReleaseDate(row: Element): LocalDate? {
        val dateText = row.selectFirst("td:nth-child(6)")?.html() ?: return null
        val (day, monthStr, year) = dateText.split(" ")
        val month = SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthStr)?.let {
            val calendar = Calendar.getInstance()
            calendar.time = it
            calendar.get(Calendar.MONTH) + 1
        } ?: return null
        return LocalDate.of(year.toInt(), month, day.toInt())
    }

    private fun parseGenres(row: Element): Set<Genre> {
        return parseTags(row, "genre") { id, name ->
            Genre(
                id = id,
                name = name
            )
        }.toSet()
    }

    private fun parseMoods(row: Element): Set<Mood> {
        return parseTags(row, "mood") { id, name ->
            Mood(
                id = id,
                name = name
            )
        }.toSet()
    }

    private fun <T> parseTags(row: Element, type: String, createInstance: (Int, String) -> T): List<T> {
        return row.selectFirst("td:nth-child(5)")?.select("a")
            ?.filter { it -> it.attr("href").contains(type) }
            ?.mapNotNull { tag ->
                /*
                tag example:
                <a class="tag" style="background-color:#ff32f7;color:#191d24" href="/music-search?genre=3">Drum &amp; Bass</a>
                <a class="tag" style="background-color:#333333" href="/music-search?mood=2">Dark</a>
                 */
                val id = tag.attr("href").split("=").last().toIntOrNull() ?: return@mapNotNull null
                createInstance(id, tag.text())
            } ?: emptyList()
    }

    private fun parseVersions(row: Element): Set<Version> {
        val versionString = row.selectFirst("td:nth-child(7)")?.html() ?: return emptySet()
        return Version.entries.filter { version ->
            versionString.uppercase().contains(version.name.uppercase())
        }.toSet()
    }
}