package com.juraj.gamebrowser.feature.detail

import com.juraj.gamebrowser.domain.error.AppError
import com.juraj.gamebrowser.domain.model.GameDetail

sealed interface GameDetailUiState {
    data object Loading : GameDetailUiState
    data class Success(val game: GameDetail) : GameDetailUiState
    data class Error(val error: AppError) : GameDetailUiState
}
