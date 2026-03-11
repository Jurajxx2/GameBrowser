package com.juraj.gamebrowser.feature.detail.di

import com.juraj.gamebrowser.domain.usecase.GetGameDetailUseCase
import com.juraj.gamebrowser.feature.detail.GameDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val featureDetailModule = module {
    factory { GetGameDetailUseCase(repository = get()) }
    viewModel { params ->
        GameDetailViewModel(
            getGameDetailUseCase = get(),
            gameId = params.get()
        )
    }
}
