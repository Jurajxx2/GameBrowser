package com.juraj.gamebrowser.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.juraj.gamebrowser.data.local.db.GameDetailDao
import com.juraj.gamebrowser.data.local.mapper.toDomain
import com.juraj.gamebrowser.data.local.mapper.toEntity
import com.juraj.gamebrowser.data.remote.api.RawgApiService
import com.juraj.gamebrowser.data.remote.mapper.toDomain
import com.juraj.gamebrowser.data.remote.paging.GamesPagingSource
import com.juraj.gamebrowser.data.util.safeApiCall
import com.juraj.gamebrowser.domain.model.Game
import com.juraj.gamebrowser.domain.model.GameDetail
import com.juraj.gamebrowser.domain.repository.GameRepository
import kotlinx.coroutines.flow.Flow

class GameRepositoryImpl(
    private val apiService: RawgApiService,
    private val gameDetailDao: GameDetailDao
) : GameRepository {

    override fun getGames(): Flow<PagingData<Game>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { GamesPagingSource(apiService) }
    ).flow

    override suspend fun getGameDetail(id: Int): Result<GameDetail> {
        // return from cache if there is anything
        gameDetailDao.getById(id)?.let { cached ->
            return Result.success(cached.toDomain())
        }

        return safeApiCall {
            val detail = apiService.getGameDetail(id).toDomain()
            gameDetailDao.insert(detail.toEntity())
            detail
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
