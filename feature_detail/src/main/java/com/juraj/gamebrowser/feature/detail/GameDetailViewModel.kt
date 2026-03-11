package com.juraj.gamebrowser.feature.detail

import androidx.lifecycle.viewModelScope
import com.juraj.gamebrowser.domain.error.AppError
import com.juraj.gamebrowser.domain.usecase.GetGameDetailUseCase
import com.juraj.gamebrowser.shared.base.ViewModelBase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameDetailViewModel(
    private val getGameDetailUseCase: GetGameDetailUseCase,
    private val gameId: Int
) : ViewModelBase() {

    private val _uiState = MutableStateFlow<GameDetailUiState>(GameDetailUiState.Loading)
    val uiState: StateFlow<GameDetailUiState> = _uiState.asStateFlow()

    init {
        loadGame()
    }

    fun retry() = loadGame()

    private fun loadGame() {
        viewModelScope.launch {
            _uiState.value = GameDetailUiState.Loading
            getGameDetailUseCase(gameId)
                .onSuccess { game -> _uiState.value = GameDetailUiState.Success(game) }
                .onFailure { error ->
                    _uiState.value = GameDetailUiState.Error(
                        error as? AppError ?: AppError.Unknown
                    )
                }
        }
    }
}
