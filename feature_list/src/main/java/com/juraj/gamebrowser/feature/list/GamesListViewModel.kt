package com.juraj.gamebrowser.feature.list

import androidx.lifecycle.viewModelScope
import com.juraj.gamebrowser.shared.base.ViewModelBase
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.juraj.gamebrowser.domain.model.Game
import com.juraj.gamebrowser.domain.usecase.GetGamesUseCase
import kotlinx.coroutines.flow.Flow

class GamesListViewModel(
    getGamesUseCase: GetGamesUseCase
) : ViewModelBase() {

    // cachedIn ensures paging data survives configuration changes
    val games: Flow<PagingData<Game>> = getGamesUseCase()
        .cachedIn(viewModelScope)
}
