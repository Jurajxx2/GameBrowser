@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.juraj.gamebrowser.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameDetailDto(
    val id: Int,
    val name: String,
    @SerialName("background_image") val backgroundImage: String?,
    val rating: Double,
    @SerialName("description_raw") val descriptionRaw: String?,
    @SerialName("ratings_count") val ratingsCount: Int,
    val released: String?,
    val metacritic: Int?
)
