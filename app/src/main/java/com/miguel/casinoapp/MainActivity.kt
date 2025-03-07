package com.miguel.casinoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.miguel.casinoapp.navigation.NavGraph
import com.miguel.casinoapp.ui.theme.CasinoAppTheme
import com.miguel.casinoapp.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    // Nombre de la clase para los logs
    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate: Actividad creada")

        setContent {
            CasinoAppTheme {
                val navController = rememberNavController()

                // Crear el ViewModel directamente
                val authViewModel: AuthViewModel = viewModel()

                Surface(color = MaterialTheme.colorScheme.background) {
                    // Pasar el authViewModel a la NavGraph
                    NavGraph(navController = navController, authViewModel = authViewModel)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "onStart: Actividad visible")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "onResume: Actividad interactiva")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "onPause: Actividad en segundo plano")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "onStop: Actividad no visible")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy: Actividad destruida")
    }
}


