package com.juraj.gamebrowser.domain.usecase

import androidx.paging.PagingData
import com.juraj.gamebrowser.domain.model.Game
import com.juraj.gamebrowser.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow

class GetGamesUseCase(private val repository: GameRepository) {
    operator fun invoke(): Flow<PagingData<Game>> = repository.getGames()
}
