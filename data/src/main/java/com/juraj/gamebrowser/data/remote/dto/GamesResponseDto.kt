package com.juraj.gamebrowser.data.remote.dto

import android.annotation.SuppressLint
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi @Serializable
data class GamesResponseDto(
    val count: Int,
    val next: String?,
    val results: List<GameDto>
)

@InternalSerializationApi @Serializable
data class GameDto(
    val id: Int,
    val name: String,
    @SerialName("background_image") val backgroundImage: String?,
    val rating: Double
)
