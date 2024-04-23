package com.ccc.ncs.feature.play

sealed interface PlayerUiState {

    object Loading : PlayerUiState

    data class Success(
        val isPlaying: Boolean,
        val hasPrevious: Boolean,
        val hasNext: Boolean,
        val position: Long,
        val duration: Long,
        val speed: Float,
        val aspectRatio: Float
    ) : PlayerUiState
}