package com.juraj.gamebrowser.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.juraj.gamebrowser.data.remote.api.RawgApiService
import com.juraj.gamebrowser.data.remote.mapper.toDomain
import com.juraj.gamebrowser.data.util.safePagingLoad
import com.juraj.gamebrowser.domain.model.Game

class GamesPagingSource(
    private val apiService: RawgApiService
) : PagingSource<Int, Game>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Game> {
        val page = params.key ?: STARTING_PAGE
        return safePagingLoad {
            val response = apiService.getGames(
                page = page,
                pageSize = params.loadSize.coerceAtMost(MAX_PAGE_SIZE)
            )
            LoadResult.Page(
                data = response.results.map { it.toDomain() },
                prevKey = if (page == STARTING_PAGE) null else page - 1,
                nextKey = if (response.next != null) page + 1 else null
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Game>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
        private const val MAX_PAGE_SIZE = 40
    }
}
