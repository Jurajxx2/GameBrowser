package com.juraj.gamebrowser.domain.usecase

import androidx.paging.PagingData
import com.juraj.gamebrowser.domain.model.Game
import com.juraj.gamebrowser.domain.repository.GameRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class GetGamesUseCaseTest {

    private lateinit var repository: GameRepository
    private lateinit var useCase: GetGamesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetGamesUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository`() {
        val pagingData = PagingData.from(listOf(Game(1, "Test Game", null, 4.5)))
        every { repository.getGames() } returns flowOf(pagingData)

        val result = useCase()

        assertNotNull(result)
        verify(exactly = 1) { repository.getGames() }
    }

    @Test
    fun `invoke returns empty paging data when repository returns empty`() {
        every { repository.getGames() } returns flowOf(PagingData.empty())

        val result = useCase()

        assertNotNull(result)
        verify(exactly = 1) { repository.getGames() }
    }
}
