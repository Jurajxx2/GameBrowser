package com.juraj.gamebrowser

import android.app.Application
import com.juraj.gamebrowser.data.di.dataModule
import com.juraj.gamebrowser.feature.detail.di.featureDetailModule
import com.juraj.gamebrowser.feature.list.di.featureListModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GameBrowserApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GameBrowserApp)
            modules(
                dataModule(apiKey = BuildConfig.RAWG_API_KEY),
                featureListModule,
                featureDetailModule
            )
        }
    }
}
