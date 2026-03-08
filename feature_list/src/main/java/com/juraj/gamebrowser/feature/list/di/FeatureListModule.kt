package com.juraj.gamebrowser.feature.list.di

import com.juraj.gamebrowser.domain.usecase.GetGamesUseCase
import com.juraj.gamebrowser.feature.list.GamesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureListModule = module {
    factory { GetGamesUseCase(repository = get()) }
    viewModel { GamesListViewModel(getGamesUseCase = get()) }
}
