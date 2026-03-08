package com.juraj.gamebrowser.domain.repository

import androidx.paging.PagingData
import com.juraj.gamebrowser.domain.model.Game
import com.juraj.gamebrowser.domain.model.GameDetail
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    fun getGames(): Flow<PagingData<Game>>
    suspend fun getGameDetail(id: Int): Result<GameDetail>
}
