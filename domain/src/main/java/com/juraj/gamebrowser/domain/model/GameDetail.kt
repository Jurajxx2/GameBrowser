package com.juraj.gamebrowser.domain.model

data class GameDetail(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val rating: Double,
    val description: String,
    val ratingsCount: Int,
    val released: String?,
    val metacritic: Int?
)
