package com.juraj.gamebrowser.domain.usecase

import com.juraj.gamebrowser.domain.model.GameDetail
import com.juraj.gamebrowser.domain.repository.GameRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetGameDetailUseCaseTest {

    private lateinit var repository: GameRepository
    private lateinit var useCase: GetGameDetailUseCase

    private val fakeGameDetail = GameDetail(
        id = 1,
        name = "Grand Theft Auto V",
        imageUrl = "https://example.com/image.jpg",
        rating = 4.47,
        description = "An open world action game.",
        ratingsCount = 5000,
        released = "2013-09-17",
        metacritic = 97
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetGameDetailUseCase(repository)
    }

    @Test
    fun `invoke returns success when repository succeeds`() = runTest {
        coEvery { repository.getGameDetail(1) } returns Result.success(fakeGameDetail)

        val result = useCase(1)

        assertTrue(result.isSuccess)
        assertEquals(fakeGameDetail, result.getOrNull())
        coVerify(exactly = 1) { repository.getGameDetail(1) }
    }

    @Test
    fun `invoke returns failure when repository throws`() = runTest {
        val exception = Exception("Network error")
        coEvery { repository.getGameDetail(1) } returns Result.failure(exception)

        val result = useCase(1)

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke passes correct id to repository`() = runTest {
        val gameId = 3498
        coEvery { repository.getGameDetail(gameId) } returns Result.success(fakeGameDetail.copy(id = gameId))

        useCase(gameId)

        coVerify(exactly = 1) { repository.getGameDetail(gameId) }
    }
}
