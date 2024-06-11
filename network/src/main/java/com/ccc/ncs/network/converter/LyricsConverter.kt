package com.ccc.ncs.network.converter

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter

class LyricsConverter: Converter<ResponseBody, String> {
    override fun convert(resonseBody: ResponseBody): String {
        val document = Jsoup.parse(resonseBody.string())

        return document.select("#mw-content-text table table td:nth-child(2) table td")
            .map { it.text() }
            .joinToString("\n")
            .trim()
    }
}