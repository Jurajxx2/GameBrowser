package com.juraj.gamebrowser.data.di

import androidx.room.Room
import com.juraj.gamebrowser.data.local.db.GameDatabase
import com.juraj.gamebrowser.data.remote.api.RawgApiService
import com.juraj.gamebrowser.data.repository.GameRepositoryImpl
import com.juraj.gamebrowser.domain.repository.GameRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun dataModule(apiKey: String) = module {

    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            install(Logging) {
                level = LogLevel.BODY
            }
            defaultRequest {
                url("https://api.rawg.io/api/")
            }
            HttpResponseValidator {
                validateResponse { response ->
                    when (response.status.value) {
                        in 400..499 -> throw ClientRequestException(response, "")
                        in 500..599 -> throw ServerResponseException(response, "")
                    }
                }
            }
        }
    }

    single { RawgApiService(client = get(), apiKey = apiKey) }

    single {
        Room.databaseBuilder(
            androidContext(),
            GameDatabase::class.java,
            "game_browser.db"
        )
            .addMigrations(GameDatabase.MIGRATION_1_2)
            .build()
    }

    single { get<GameDatabase>().gameDetailDao() }

    single<GameRepository> { GameRepositoryImpl(apiService = get(), gameDetailDao = get()) }
}
