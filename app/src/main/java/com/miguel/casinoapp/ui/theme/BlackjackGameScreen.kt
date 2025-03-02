package com.miguel.casinoapp.ui.theme


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.miguel.casinoapp.R
import com.miguel.casinoapp.R.drawable.ic_home
import com.miguel.casinoapp.viewmodel.BlackjackViewModel

@Composable
fun BlackjackGameScreen(viewModel: BlackjackViewModel, navController: NavHostController) {
    BlackjackWithButtonAndImage(viewModel = viewModel, navController = navController)
}

@Composable
fun BlackjackWithButtonAndImage(viewModel: BlackjackViewModel, navController: NavHostController) {
    val state by viewModel.state.collectAsState()

    val tableBackground = Color(0xFF006400) // Verde oscuro para la mesa
    val woodColor = Color(0xFF8B4513)       // Color madera para el borde

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(tableBackground)
            .border(12.dp, woodColor)
            .padding(16.dp)
    ) {
        // Botón para regresar al menú principal
        IconButton(
            onClick = {
                navController.navigate("mainMenu") {
                    popUpTo("mainMenu") { inclusive = true }
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 25.dp, start = 8.dp)
        ) {
            Image(
                painter = painterResource(id = ic_home),
                contentDescription = "Botón Imagen",
                modifier = Modifier.size(48.dp)
            )
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Título del juego
            Text(
                text = "Blackjack",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            // Mano del crupier
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Crupier", style = MaterialTheme.typography.titleMedium, color = Color.White)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    state.dealerHand.forEach { card ->
                        ImageCard(card.value, card.suit, card.image)  // Aquí pasas imageUrl
                    }
                }
                Text(
                    text = "Puntos del Crupier: ${state.dealerScore}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }

            // Mano del jugador
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Jugador", style = MaterialTheme.typography.titleMedium, color = Color.White)

                if (state.playerHand.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        state.playerHand.forEach { card ->
                            ImageCard(card.value, card.suit, card.image)  // Aquí pasas imageUrl
                        }
                    }
                } else {
                    Text("No hay cartas en tu mano", color = Color.White)
                }

                Text(
                    text = "Tus puntos: ${state.playerScore}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }


            // Botones de acción
            if (state.gameResult == null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { viewModel.playerHit() },
                        enabled = state.gameResult == null, // Solo habilitar si el juego no ha terminado
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50), // Verde
                            contentColor = Color.White
                        )
                    ) {
                        Text("Pedir carta")
                    }

                    Button(
                        onClick = { viewModel.playerStand() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F), // Rojo
                            contentColor = Color.White
                        )
                    ) {
                        Text("Plantarse")
                    }
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Resultado: ${state.gameResult}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.Yellow
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.resetGame() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E3A8A), // azul
                            contentColor = Color.White
                        )
                    ) {
                        Text("Reiniciar partida")
                    }
                }
            }
        }
    }
}

@Composable
fun ImageCard(value: String, suit: String, imageUrl: String?) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "$value of $suit",
                modifier = Modifier.size(96.dp)
            )
        } else {
            // Mostrar imagen de marcador de posición
            Image(
                painter = painterResource(id = R.drawable.error), // Reemplaza con tu imagen
                contentDescription = "$value of $suit",
                modifier = Modifier.size(96.dp)
            )
        }
    }
}

//@Composable
//fun PlayerHand(cards: List<Card>) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        horizontalArrangement = Arrangement.Center
//    ) {
//        cards.forEachIndexed { index, card ->
//            AnimatedVisibility(
//                visible = true,
//                enter = fadeIn(animationSpec = tween(500, delayMillis = index * 200)) +
//                        slideInHorizontally(initialOffsetX = { it / 4 })
//            ) {
//                ImageCard(card.value, card.suit, card.image)
//            }
//        }
//    }
//}

