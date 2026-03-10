@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.juraj.gamebrowser.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GamesResponseDto(
    val count: Int,
    val next: String?,
    val results: List<GameDto>
)

@Serializable
data class GameDto(
    val id: Int,
    val name: String,
    @SerialName("background_image") val backgroundImage: String?,
    val rating: Double
)
