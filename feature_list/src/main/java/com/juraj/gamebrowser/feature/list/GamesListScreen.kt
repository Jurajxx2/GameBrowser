package com.juraj.gamebrowser.feature.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.juraj.gamebrowser.domain.model.Game
import com.juraj.gamebrowser.shared.R
import com.juraj.gamebrowser.shared.components.ErrorState
import com.juraj.gamebrowser.shared.components.RatingBadge
import com.juraj.gamebrowser.shared.components.shimmerEffect
import com.juraj.gamebrowser.shared.error.toUserMessage
import com.juraj.gamebrowser.shared.theme.GameBrowserTheme
import com.juraj.gamebrowser.shared.theme.Surface
import com.juraj.gamebrowser.shared.theme.spacing
import kotlinx.coroutines.flow.flowOf
import org.koin.androidx.compose.koinViewModel

@Composable
fun GamesListScreen(
    onGameClick: (Int) -> Unit,
    viewModel: GamesListViewModel = koinViewModel()
) {
    val games = viewModel.games.collectAsLazyPagingItems()
    GamesListContent(games = games, onGameClick = onGameClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GamesListContent(
    games: LazyPagingItems<Game>,
    onGameClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.games_list_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        when (val refresh = games.loadState.refresh) {
            is LoadState.Loading -> {
                // Show shimmer placeholders while loading the first page
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(MaterialTheme.spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    items(6) { GameListItemShimmer() }
                }
            }
            is LoadState.Error -> ErrorState(
                message = refresh.error.toUserMessage(),
                onRetry = { games.retry() }
            )
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(MaterialTheme.spacing.medium),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    items(games.itemCount) { index ->
                        games[index]?.let { game ->
                            GameListItem(game = game, onClick = { onGameClick(game.id) })
                        }
                    }

                    // Footer: loading more / append error
                    when (val append = games.loadState.append) {
                        is LoadState.Loading -> item {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(MaterialTheme.spacing.medium),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                        is LoadState.Error -> item {
                            ErrorState(
                                message = append.error.toUserMessage(),
                                onRetry = { games.retry() },
                                modifier = Modifier.height(120.dp)
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
private fun GameListItem(
    game: Game,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = game.imageUrl,
                contentDescription = game.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.xSmall))
                RatingBadge(rating = game.rating)
            }
        }
    }
}

@Composable
private fun GameListItemShimmer(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.xSmall))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F23)
@Composable
private fun GamesListContentPreview() {
    GameBrowserTheme {
        val fakeGames = listOf(
            Game(1, "Grand Theft Auto V", null, 4.47),
            Game(2, "The Witcher 3: Wild Hunt", null, 4.66),
            Game(3, "Portal 2", null, 4.51)
        )
        GamesListContent(
            games = flowOf(PagingData.from(fakeGames)).collectAsLazyPagingItems(),
            onGameClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F23)
@Composable
private fun GameListItemPreview() {
    GameBrowserTheme {
        GameListItem(
            game = Game(1, "Grand Theft Auto V", null, 4.47),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F23)
@Composable
private fun GameListItemShimmerPreview() {
    GameBrowserTheme {
        GameListItemShimmer()
    }
}
