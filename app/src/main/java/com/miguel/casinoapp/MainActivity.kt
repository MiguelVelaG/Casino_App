package com.miguel.casinoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.miguel.casinoapp.navigation.NavGraph
import com.miguel.casinoapp.ui.theme.CasinoAppTheme

class MainActivity : ComponentActivity() {

    // Nombre de la clase para los logs
    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate: Actividad creada")
        setContent {
            CasinoAppTheme {
                val navController = rememberNavController()
                Surface(color = MaterialTheme.colorScheme.background) {
                    NavGraph(navController = navController)
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


