package com.ccc.ncs.cache

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.ContentMetadata

@OptIn(UnstableApi::class)
fun Cache.isFullyCached(key: String): Boolean {
    val contentLength = this
        .getContentMetadata(key)
        .get(ContentMetadata.KEY_CONTENT_LENGTH, -1L)

    val cachedByte = this.getCachedBytes(key, 0, contentLength)

    return cachedByte == contentLength
}