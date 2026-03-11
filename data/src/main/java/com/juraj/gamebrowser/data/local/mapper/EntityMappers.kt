package com.juraj.gamebrowser.data.local.mapper

import com.juraj.gamebrowser.data.local.entity.GameDetailEntity
import com.juraj.gamebrowser.domain.model.GameDetail

fun GameDetailEntity.toDomain() = GameDetail(
    id = id,
    name = name,
    imageUrl = imageUrl,
    rating = rating,
    description = description,
    ratingsCount = ratingsCount,
    released = released,
    metacritic = metacritic
)

fun GameDetail.toEntity() = GameDetailEntity(
    id = id,
    name = name,
    imageUrl = imageUrl,
    rating = rating,
    description = description,
    ratingsCount = ratingsCount,
    released = released,
    metacritic = metacritic,
    cachedAt = System.currentTimeMillis()
)
