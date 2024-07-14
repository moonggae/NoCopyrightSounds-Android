package com.ccc.ncs.network.util

import android.net.Uri

fun joinUri(
    baseUrl: String,
    vararg pathSegments: String
): Uri {
    val uriBuilder = Uri.parse(baseUrl).buildUpon()
    pathSegments.forEach { segment ->
        uriBuilder.appendPath(segment)
    }
    return uriBuilder.build()
}