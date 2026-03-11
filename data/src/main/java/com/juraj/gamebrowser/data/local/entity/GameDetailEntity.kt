package com.juraj.gamebrowser.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_details")
data class GameDetailEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String?,
    val rating: Double,
    val description: String,
    val ratingsCount: Int,
    val released: String?,
    val metacritic: Int?,
    val cachedAt: Long
)
