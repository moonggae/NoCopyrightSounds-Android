package com.ccc.ncs.domain.repository

import com.ccc.ncs.model.RecentSearchQuery
import kotlinx.coroutines.flow.Flow

/**
 * Data layer interface for the recent searches.
 */
interface RecentSearchRepository {

    /**
     * Get the recent search queries up to the number of queries specified as [limit].
     */
    fun getRecentSearchQueries(limit: Int): Flow<List<RecentSearchQuery>>

    /**
     * Insert or replace the [searchQuery] as part of the recent searches.
     */
    suspend fun insertOrReplaceRecentSearch(searchQuery: String)

    /**
     * Delete a recent search.
     */
    suspend fun deleteRecentSearch(searchQuery: String)
}