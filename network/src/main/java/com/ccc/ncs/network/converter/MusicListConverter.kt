package com.ccc.ncs.network.converter

import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.Version
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import retrofit2.Converter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

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

    private fun parseMusicList(document: Document): List<Music> {
        val musicTable = findMusicTable(document)
        return musicTable?.select("tbody > tr")?.mapNotNull { row ->
            parseMusicRow(row)
        }?: emptyList()
    }

    private fun parseMusicRow(row: Element): Music? {
        val titleTag = row.selectFirst("td:nth-child(1) > a") ?: return null
        val artistDetailUrl = parseArtistDetailUrl(titleTag)
        val releaseDate = parseReleaseDate(row) ?: return null
        val genres = parseGenres(row)
        val moods = parseMoods(row)
        val versions = parseVersions(row)

        return Music(
            title = titleTag.attribute("data-track").value,
            artist = titleTag.attribute("data-artistraw").value,
            dataUrl = titleTag.attribute("data-url").value,
            artistDetailUrl = artistDetailUrl,
            coverThumbnailUrl = titleTag.attribute("data-cover").value,
            coverUrl = titleTag.attribute("data-cover").value.replace("100x100", "325x325"),
            detailUrl = row.selectFirst("td:nth-child(3) > a")?.attr("href") ?: "",
            releaseDate = releaseDate,
            genres = genres.toSet(),
            moods = moods.toSet(),
            versions = versions.toSet()
        )
    }

    private fun parseArtistDetailUrl(tag: Element): String {
        val artistTagHtml = tag.attribute("data-artist").html()
        return Jsoup.parse(artistTagHtml).select("a").attr("href")
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
            Genre(id = id, name = name)
        }.toSet()
    }

    private fun parseMoods(row: Element): Set<Mood> {
        return parseTags(row, "mood") { id, name ->
            Mood(id = id, name = name)
        }.toSet()
    }

    private fun <T> parseTags(row: Element, type: String, createInstance: (Int, String) -> T): List<T> {
        return row.selectFirst("td:nth-child(5)")?.select("a")
            ?.filter { it -> it.attr("href").contains(type) }
            ?.mapNotNull { tag ->
                val id = tag.attr("href").split("=").last().toIntOrNull() ?: return@mapNotNull null
                createInstance(id, tag.html())
            } ?: emptyList()
    }

    private fun parseVersions(row: Element): Set<Version> {
        val versionString = row.selectFirst("td:nth-child(7)")?.html() ?: return emptySet()
        return Version.entries.filter { version ->
            versionString.uppercase().contains(version.name.uppercase())
        }.toSet()
    }
}