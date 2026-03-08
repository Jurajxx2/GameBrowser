package com.juraj.gamebrowser.feature.detail

import androidx.lifecycle.viewModelScope
import com.juraj.gamebrowser.shared.base.ViewModelBase
import com.juraj.gamebrowser.domain.error.AppError
import com.juraj.gamebrowser.domain.usecase.GetGameDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameDetailViewModel(
    private val getGameDetailUseCase: GetGameDetailUseCase
) : ViewModelBase() {

    private val _uiState = MutableStateFlow<GameDetailUiState>(GameDetailUiState.Loading)
    val uiState: StateFlow<GameDetailUiState> = _uiState.asStateFlow()

    fun loadGame(id: Int) {
        viewModelScope.launch {
            _uiState.value = GameDetailUiState.Loading
            getGameDetailUseCase(id)
                .onSuccess { game -> _uiState.value = GameDetailUiState.Success(game) }
                .onFailure { error ->
                    _uiState.value = GameDetailUiState.Error(
                        error as? AppError ?: AppError.Unknown
                    )
                }
        }
    }
}
