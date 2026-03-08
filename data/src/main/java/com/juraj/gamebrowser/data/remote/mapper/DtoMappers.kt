package com.juraj.gamebrowser.data.remote.mapper

import com.juraj.gamebrowser.data.remote.dto.GameDetailDto
import com.juraj.gamebrowser.data.remote.dto.GameDto
import com.juraj.gamebrowser.domain.model.Game
import com.juraj.gamebrowser.domain.model.GameDetail
import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
fun GameDto.toDomain() = Game(
    id = id,
    name = name,
    imageUrl = backgroundImage,
    rating = rating
)

@OptIn(InternalSerializationApi::class)
fun GameDetailDto.toDomain() = GameDetail(
    id = id,
    name = name,
    imageUrl = backgroundImage,
    rating = rating,
    description = descriptionRaw.orEmpty(),
    ratingsCount = ratingsCount,
    released = released,
    metacritic = metacritic
)
