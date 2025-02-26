/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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


