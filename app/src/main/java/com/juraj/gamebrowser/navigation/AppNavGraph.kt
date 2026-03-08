package com.juraj.gamebrowser.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.juraj.gamebrowser.feature.detail.GameDetailScreen
import com.juraj.gamebrowser.feature.list.GamesListScreen

private const val ROUTE_GAMES_LIST = "games_list"
private const val ROUTE_GAME_DETAIL = "game_detail/{gameId}"
private const val ARG_GAME_ID = "gameId"

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ROUTE_GAMES_LIST,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
    ) {
        composable(ROUTE_GAMES_LIST) {
            GamesListScreen(
                onGameClick = { gameId ->
                    navController.navigate("game_detail/$gameId")
                }
            )
        }

        composable(
            route = ROUTE_GAME_DETAIL,
            arguments = listOf(navArgument(ARG_GAME_ID) { type = NavType.IntType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getInt(ARG_GAME_ID) ?: return@composable
            GameDetailScreen(
                gameId = gameId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
