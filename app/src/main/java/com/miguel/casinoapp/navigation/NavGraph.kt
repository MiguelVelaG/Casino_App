package com.miguel.casinoapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.miguel.casinoapp.ui.theme.DiceGameScreen
import com.miguel.casinoapp.viewmodel.DiceRollerViewModel
import com.miguel.casinoapp.ui.theme.MainMenuScreen


@Composable
fun NavGraph(navController: NavHostController) {
    // Define el grafo de navegación entre las pantallas
    NavHost(navController = navController, startDestination = "mainMenu") {
        composable("mainMenu") {
            // Pantalla de inicio
            MainMenuScreen(navController = navController)
        }
        composable("dice") {
            // Se obtiene el ViewModel del juego de dados correctamente
            val diceRollerViewModel: DiceRollerViewModel = viewModel() // Creación del ViewModel
            // Se pasa el ViewModel y el NavController a la pantalla correspondiente
            DiceGameScreen(viewModel = diceRollerViewModel, navController = navController)
        }
    }
}