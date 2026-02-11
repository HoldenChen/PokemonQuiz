package com.holden.pokemonquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.holden.pokemonquiz.di.AppContainer
import com.holden.pokemonquiz.ui.detail.DetailScreen
import com.holden.pokemonquiz.ui.search.SearchScreen
import com.holden.pokemonquiz.ui.splash.SplashScreen
import com.holden.pokemonquiz.ui.splash.isFirstLaunch
import com.holden.pokemonquiz.ui.theme.PokemonQuizTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val showSplash = isFirstLaunch(this)

        setContent {
            PokemonQuizTheme {
                val navController = rememberNavController()
                val storeManager = remember { AppContainer.storeManager }
                val startDestination = if (showSplash) "splash" else "search"

                NavHost(navController = navController, startDestination = startDestination) {
                    composable("splash") {
                        SplashScreen(
                                onDismiss = {
                                    navController.navigate("search") {
                                        popUpTo("splash") { inclusive = true }
                                    }
                                }
                        )
                    }

                    composable("search") {
                        SearchScreen(
                                store = storeManager.searchStore,
                                onPokemonClick = { pokemonId ->
                                    navController.navigate("detail/$pokemonId")
                                }
                        )
                    }

                    composable(
                            route = "detail/{pokemonId}",
                            arguments = listOf(navArgument("pokemonId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: 0
                        DetailScreen(
                                pokemonId = pokemonId,
                                store = storeManager.detailStore,
                                onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
