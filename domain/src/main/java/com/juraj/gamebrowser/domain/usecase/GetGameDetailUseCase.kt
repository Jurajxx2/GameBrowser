package com.juraj.gamebrowser.domain.usecase

import com.juraj.gamebrowser.domain.model.GameDetail
import com.juraj.gamebrowser.domain.repository.GameRepository

class GetGameDetailUseCase(private val repository: GameRepository) {
    suspend operator fun invoke(id: Int): Result<GameDetail> = repository.getGameDetail(id)
}
