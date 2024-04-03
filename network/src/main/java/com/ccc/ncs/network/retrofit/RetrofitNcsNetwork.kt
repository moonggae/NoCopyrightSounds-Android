package com.ccc.ncs.network.retrofit


import com.ccc.ncs.model.Genre
import com.ccc.ncs.model.Mood
import com.ccc.ncs.model.Music
import com.ccc.ncs.model.Version
import com.ccc.ncs.network.BuildConfig
import com.ccc.ncs.network.NcsNetworkDataSource
import okhttp3.Call
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


private interface RetrofitNcsNetworkApi {
    @GET("/music-search")
    suspend fun getMusicListHtml(
        @Query("page") page: Int,
        @Query("q") query: String? = null,
        @Query("genre") genreId: Int? = null,
        @Query("mood") moodId: Int? = null,
        @Query("version") version: String? = null,
        @Query("display") display: String = "list"
    ): Response<String>
}

private const val WEB_URL = BuildConfig.WEB_URL

@Singleton
class RetrofitNcsNetwork @Inject constructor(
    okHttpCallFactory: Call.Factory
) : NcsNetworkDataSource {
    private val networkApi by lazy {
        Retrofit.Builder()
            .baseUrl(WEB_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .callFactory { okHttpCallFactory.newCall(it) }
            .build()
            .create(RetrofitNcsNetworkApi::class.java)
    }

    override suspend fun getMusics(
        page: Int,
        query: String?,
        genreId: Int?,
        moodId: Int?,
        version: String?
    ): List<Music> {
        val htmlString = fetchMusicListHtml(page, query, genreId, moodId, version) ?: return emptyList()

        val document = Jsoup.parse(htmlString)
        val musicTable = findMusicTable(document) ?: return emptyList()

        return parseMusicRows(musicTable)
    }

    private suspend fun fetchMusicListHtml(
        page: Int,
        query: String?,
        genreId: Int?,
        moodId: Int?,
        version: String?
    ): String? {
        return networkApi.getMusicListHtml(page, query, genreId, moodId, version).body()
    }

    private fun findMusicTable(doc: Document): Element? {
        return doc.select("body > main > article.module.table").firstOrNull { element ->
            !element.classNames().contains("featured")
        }
    }

    private fun parseMusicRows(table: Element): List<Music> {
        return table.select("tbody > tr").mapNotNull { row ->
            parseMusicRow(row)
        }
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
        return Version.values().filter { version ->
            versionString.uppercase().contains(version.name.uppercase())
        }.toSet()
    }



}