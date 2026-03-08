package com.juraj.gamebrowser.data.remote.api

import com.juraj.gamebrowser.data.remote.dto.GameDetailDto
import com.juraj.gamebrowser.data.remote.dto.GamesResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RawgApiService(
    private val client: HttpClient,
    private val apiKey: String,
) {
    suspend fun getGames(page: Int, pageSize: Int = 20): GamesResponseDto =
        client.get("games") {
            parameter("key", apiKey)
            parameter("page", page)
            parameter("page_size", pageSize)
        }.body()

    suspend fun getGameDetail(id: Int): GameDetailDto =
        client.get("games/$id") {
            parameter("key", apiKey)
        }.body()
}
