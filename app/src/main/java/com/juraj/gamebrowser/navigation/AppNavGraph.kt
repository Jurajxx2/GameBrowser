package com.juraj.gamebrowser.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.juraj.gamebrowser.feature.detail.GameDetailScreen
import com.juraj.gamebrowser.feature.list.GamesListScreen
import kotlinx.serialization.Serializable

@Serializable
data object GamesListRoute

@Serializable
data class GameDetailRoute(val gameId: Int)

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = GamesListRoute,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
    ) {
        composable<GamesListRoute> {
            GamesListScreen(
                onGameClick = { gameId ->
                    navController.navigate(GameDetailRoute(gameId))
                }
            )
        }

        composable<GameDetailRoute> { backStackEntry ->
            val route: GameDetailRoute = backStackEntry.toRoute()
            GameDetailScreen(
                gameId = route.gameId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
