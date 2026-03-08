package com.juraj.gamebrowser.feature.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.juraj.gamebrowser.domain.error.AppError
import com.juraj.gamebrowser.domain.model.GameDetail
import com.juraj.gamebrowser.shared.R
import com.juraj.gamebrowser.shared.components.ErrorState
import com.juraj.gamebrowser.shared.components.LoadingIndicator
import com.juraj.gamebrowser.shared.components.RatingBadge
import com.juraj.gamebrowser.shared.error.toUserMessage
import com.juraj.gamebrowser.shared.theme.GameBrowserTheme
import com.juraj.gamebrowser.shared.theme.spacing
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameDetailScreen(
    gameId: Int,
    onBack: () -> Unit,
    viewModel: GameDetailViewModel = koinViewModel()
) {
    LaunchedEffect(gameId) {
        viewModel.loadGame(gameId)
    }
    val uiState by viewModel.uiState.collectAsState()
    GameDetailContent(uiState = uiState, onBack = onBack, onRetry = { viewModel.loadGame(gameId) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameDetailContent(
    uiState: GameDetailUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is GameDetailUiState.Success) {
                        Text(
                            text = uiState.game.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.game_detail_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is GameDetailUiState.Loading -> LoadingIndicator()
                is GameDetailUiState.Error -> ErrorState(
                    message = uiState.error.toUserMessage(),
                    onRetry = onRetry
                )
                is GameDetailUiState.Success -> GameDetailBody(game = uiState.game)
            }
        }
    }
}

@Composable
private fun GameDetailBody(game: GameDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = game.imageUrl,
            contentDescription = game.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )

        Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
            Text(
                text = game.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Row(verticalAlignment = Alignment.CenterVertically) {
                RatingBadge(rating = game.rating)

                game.metacritic?.let { score ->
                    Text(
                        text = stringResource(R.string.game_detail_metacritic, score),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = MaterialTheme.spacing.small)
                    )
                }
            }

            game.released?.let { date ->
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.xSmall))
                Text(
                    text = stringResource(R.string.game_detail_released, date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = stringResource(R.string.game_detail_ratings_count, game.ratingsCount),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            Text(
                text = game.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F23)
@Composable
private fun GameDetailSuccessPreview() {
    GameBrowserTheme {
        GameDetailContent(
            uiState = GameDetailUiState.Success(
                GameDetail(
                    id = 1,
                    name = "The Witcher 3: Wild Hunt",
                    imageUrl = null,
                    rating = 4.66,
                    description = "The Witcher 3: Wild Hunt is an action role-playing game developed and published by CD Projekt Red.",
                    ratingsCount = 8523,
                    released = "2015-05-18",
                    metacritic = 92
                )
            ),
            onBack = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F23)
@Composable
private fun GameDetailErrorPreview() {
    GameBrowserTheme {
        GameDetailContent(
            uiState = GameDetailUiState.Error(AppError.NoInternet),
            onBack = {},
            onRetry = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F23)
@Composable
private fun GameDetailLoadingPreview() {
    GameBrowserTheme {
        GameDetailContent(
            uiState = GameDetailUiState.Loading,
            onBack = {},
            onRetry = {}
        )
    }
}
