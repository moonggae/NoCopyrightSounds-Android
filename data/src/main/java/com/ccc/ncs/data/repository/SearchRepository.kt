package com.ccc.ncs.data.repository

import kotlinx.coroutines.flow.MutableStateFlow

interface SearchRepository {
    val currentSearchQuery: MutableStateFlow<String?>
}