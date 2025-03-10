package com.miguel.casinoapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.miguel.casinoapp.ui.theme.BlackjackGameScreen
import com.miguel.casinoapp.ui.theme.DescriptionScreen
import com.miguel.casinoapp.ui.theme.DiceGameScreen
import com.miguel.casinoapp.ui.theme.LoginScreen
import com.miguel.casinoapp.viewmodel.DiceRollerViewModel
import com.miguel.casinoapp.ui.theme.MainMenuScreen
import com.miguel.casinoapp.ui.theme.RegisterScreen
import com.miguel.casinoapp.ui.theme.RouletteGameScreen
import com.miguel.casinoapp.ui.theme.UserProfileScreen
import com.miguel.casinoapp.viewmodel.AuthViewModel
import com.miguel.casinoapp.viewmodel.BlackjackViewModel
import com.miguel.casinoapp.viewmodel.RouletteViewModel

@Composable
fun NavGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }

        composable("register") {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }

        composable("mainMenu") {
            MainMenuScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("userProfile") {
            UserProfileScreen(navController = navController, authViewModel = authViewModel)
        }

        composable("dice") {
            val diceRollerViewModel: DiceRollerViewModel = viewModel()
            DiceGameScreen(viewModel = diceRollerViewModel, navController = navController)
        }
        composable("roulette") {
            val rouletteViewModel: RouletteViewModel = viewModel()
            RouletteGameScreen(viewModel = rouletteViewModel, navController = navController)
        }
        composable("blackjack") {
            val blackjackViewModel: BlackjackViewModel = viewModel()
            BlackjackGameScreen(viewModel = blackjackViewModel, navController = navController)
        }
        composable("description") {
            DescriptionScreen(navController = navController)
        }
    }
}
