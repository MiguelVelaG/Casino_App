package com.miguel.casinoapp.ui.theme

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.miguel.casinoapp.R.drawable.bola_ruleta
import com.miguel.casinoapp.R.drawable.coin
import com.miguel.casinoapp.R.drawable.ic_home
import com.miguel.casinoapp.R.drawable.roulette_wheel_eu
import com.miguel.casinoapp.R.raw.roulette_spin
import com.miguel.casinoapp.viewmodel.RouletteViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun RouletteGameScreen(viewModel: RouletteViewModel, navController: NavHostController) {
    RouletteWithButtonAndImage(viewModel = viewModel, navController = navController)
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun RouletteWithButtonAndImage(viewModel: RouletteViewModel, navController: NavHostController) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == 2
    val coins by viewModel.coins
    val betAmount by viewModel.betAmount
    val selectedBet by viewModel.selectedBet
    val selectedNumber by viewModel.selectedNumber // Estado para número seleccionado
    val betResult by viewModel.betResult


    // Estado animado para el ángulo de rotación de la ruleta
    val rotationAngle by animateFloatAsState(
        targetValue = viewModel.rotationAngle.value, // Actualiza el ángulo de rotación de la ruleta
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        label = "" // Define la duración y el easing
    )
    // Estado animado para la posición de la bola
    val ballPosition by animateFloatAsState(
        targetValue = viewModel.ballPosition.value, // Actualiza la posición de la bola
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        label = "" // Controla el movimiento suave de la bola
    )

    // State para la rotación de la ruleta y la bola
    val mediaPlayer = MediaPlayer.create(LocalContext.current, roulette_spin)
    val rouletteImage = painterResource(id = roulette_wheel_eu)
    val ballImage = painterResource(id = bola_ruleta)

    val rouletteCenterX = 0f // Ajusta este valor si la ruleta no está centrada
    val rouletteCenterY = -10f // Ajusta este valor si la ruleta no está centrada
    // Tamaño de la ruleta
    val rouletteSize = 300.dp
    val radius = (rouletteSize.value / 2) * 0.73f  // Radio para posicionar la bola dentro de la ruleta
    val angleRadians = Math.toRadians(ballPosition.toDouble())

    val verticalOffset = -169f // Ajusta este valor para subir la bola

// Coordenadas X e Y de la bola en función del ángulo y radio
    val ballX = (rouletteCenterX + radius * cos(angleRadians)).toFloat()
    val ballY = (rouletteCenterY + radius * sin(angleRadians) + verticalOffset).toFloat()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF006400)) // Fondo verde
            .border(8.dp, Color(0xFF8B4513)) // Borde de madera
            .padding(10.dp)
    ) {
        // Botón de regreso
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

        if (isLandscape) {
            // Vista horizontal
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 45.dp), // Ajuste para mover todo hacia abajo
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {
                // Columna izquierda: Monedas y Ruleta
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Mostrar monedas
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = coin),
                            contentDescription = "Monedas",
                            modifier = Modifier.size(40.dp)
                        )
                        Text(
                            "Monedas: $coins",
                            fontSize = 20.sp,
                            color = Color.Yellow,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Ruleta girando
                    Image(
                        painter = rouletteImage,
                        contentDescription = "Ruleta",
                        modifier = Modifier
                            .size(250.dp)
                            .rotate(rotationAngle)
                    )
                    // Ajusta la posición inicial de la bola considerando el centro de la ruleta
                    Image(
                        painter = ballImage,
                        contentDescription = "Bola",
                        modifier = Modifier
                            .offset(
                                x = ballX.dp, // Posición horizontal calculada
                                y = ballY.dp    // Posición vertical calculada
                            )
                            .size(20.dp)
                    )

                }

                // Columna derecha: Apuesta y botones
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Números para apostar
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items((0..36).toList()) { i ->
                            Button(
                                onClick = { viewModel.updateSelectedNumber(i) }, // Actualizar número seleccionado
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedNumber == i) Color(0xFFFFD700) else getColorForNumber(i) // Cambiar a dorado si es seleccionado
                                ),
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Text(text = i.toString(), color = Color.White, fontSize = 18.sp)
                            }
                        }
                    }

                    // Botones para seleccionar la apuesta (rojo, negro, verde)
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        // Rojo
                        Button(
                            onClick = { viewModel.updateSelectedBet("rojo") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedBet == "rojo") Color(0xFFFFD700) else Color.Red
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Text(text = "Rojo", color = Color.White, fontSize = 18.sp)
                        }

                        // Verde
                        Button(
                            onClick = { viewModel.updateSelectedBet("verde") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedBet == "verde") Color(0xFFFFD700) else Color.Green
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Text(text = "Verde", color = Color.White, fontSize = 18.sp)
                        }

                        // Negro
                        Button(
                            onClick = { viewModel.updateSelectedBet("negro") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedBet == "negro") Color(0xFFFFD700) else Color.Black
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                        ) {
                            Text(text = "Negro", color = Color.White, fontSize = 18.sp)
                        }
                    }

                    // Mostrar cantidad apostada
                    Text(
                        "Cantidad a apostar: $betAmount",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    // Ajuste de la apuesta
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { if (betAmount >= 100) viewModel.updateBetAmount(betAmount - 100) }) {
                            Text("-100", fontSize = 18.sp)
                        }
                        Button(onClick = { if (betAmount >= 10) viewModel.updateBetAmount(betAmount - 10) }) {
                            Text("-10", fontSize = 18.sp)
                        }

                        Text(
                            "$betAmount",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            color = Color.White
                        )

                        Button(onClick = { if (betAmount + 10 <= coins) viewModel.updateBetAmount(betAmount + 10) }) {
                            Text("+10", fontSize = 18.sp)
                        }
                        Button(onClick = { if (betAmount + 100 <= coins) viewModel.updateBetAmount(betAmount + 100) }) {
                            Text("+100", fontSize = 18.sp)
                        }
                    }

                    // Botón para lanzar la ruleta
                    Button(
                        onClick = {
                            // Generar un ángulo aleatorio para la ruleta
                            viewModel.updateRotationAngle((0..360).random().toFloat())
                            viewModel.updateBallPosition((0..360).random().toFloat()) // Generar una nueva posición para la bola

                            // Iniciar sonido de la ruleta (si tienes un MediaPlayer configurado)
                            mediaPlayer.start()

                            // Llamada a la lógica de la ruleta en tu ViewModel
                            viewModel.spinRoulette(coins, betAmount, selectedBet, mediaPlayer)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A), contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Text("Girar Ruleta", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Vista vertical
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Espacio vacío para mover la ruleta más abajo
                Spacer(modifier = Modifier.height(100.dp)) // Mueve la ruleta más abajo

                // Ruleta girando
                Image(
                    painter = rouletteImage,
                    contentDescription = "Ruleta",
                    modifier = Modifier
                        .size(300.dp)
                        .rotate(rotationAngle)
                )
                // Bola animada dentro de la ruleta
                Image(
                    painter = ballImage,
                    contentDescription = "Bola",
                    modifier = Modifier
                        .offset(x = ballX.dp, // Posición horizontal calculada
                            y = ballY.dp  // Posición vertical calculada
                        )
                        .size(20.dp)
                )

                // Resultado de la apuesta
                Text(
                    betResult,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Mostrar monedas
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = coin),
                        contentDescription = "Monedas",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        "Monedas: $coins",
                        fontSize = 20.sp,
                        color = Color.Yellow,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Números para apostar
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items((0..36).toList()) { i ->
                        Button(
                            onClick = { viewModel.updateSelectedNumber(i) }, // Actualizar número seleccionado
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedNumber == i) Color(0xFFFFD700) else getColorForNumber(i) // Cambiar a dorado si es seleccionado
                            ),
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text(text = i.toString(), color = Color.White, fontSize = 18.sp)
                        }
                    }
                }

                // Botones para seleccionar la apuesta (rojo, negro, verde)
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Rojo
                    Button(
                        onClick = { viewModel.updateSelectedBet("rojo") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedBet == "rojo") Color(0xFFFFD700) else Color.Red
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text(text = "Rojo", color = Color.White, fontSize = 18.sp)
                    }

                    // Verde
                    Button(
                        onClick = { viewModel.updateSelectedBet("verde") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedBet == "verde") Color(0xFFFFD700) else Color.Green
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text(text = "Verde", color = Color.White, fontSize = 18.sp)
                    }

                    // Negro
                    Button(
                        onClick = { viewModel.updateSelectedBet("negro") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedBet == "negro") Color(0xFFFFD700) else Color.Black
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text(text = "Negro", color = Color.White, fontSize = 18.sp)
                    }
                }

                // Mostrar cantidad apostada
                Text(
                    "Cantidad a apostar: $betAmount",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                // Ajuste de la apuesta
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { if (betAmount >= 100) viewModel.updateBetAmount(betAmount - 100) }) {
                        Text("-100", fontSize = 18.sp)
                    }
                    Button(onClick = { if (betAmount >= 10) viewModel.updateBetAmount(betAmount - 10) }) {
                        Text("-10", fontSize = 18.sp)
                    }

                    Text(
                        "$betAmount",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        color = Color.White
                    )

                    Button(onClick = { if (betAmount + 10 <= coins) viewModel.updateBetAmount(betAmount + 10) }) {
                        Text("+10", fontSize = 18.sp)
                    }
                    Button(onClick = { if (betAmount + 100 <= coins) viewModel.updateBetAmount(betAmount + 100) }) {
                        Text("+100", fontSize = 18.sp)
                    }
                }

                // Botón para lanzar la ruleta
                Button(
                    onClick = {
                        // Generar un ángulo aleatorio para la ruleta
                        viewModel.updateRotationAngle((0..360).random().toFloat())
                        viewModel.updateBallPosition((0..360).random().toFloat()) // Generar una nueva posición para la bola

                        // Iniciar sonido de la ruleta (si tienes un MediaPlayer configurado)
                        mediaPlayer.start()

                        // Llamada a la lógica de la ruleta en tu ViewModel
                        viewModel.spinRoulette(coins, betAmount, selectedBet, mediaPlayer)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A), contentColor = Color.White),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text("Girar Ruleta", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun getColorForNumber(number: Int): Color {
    // Devuelve el color dependiendo del número
    return when (number) {
        0 -> Color.Green
        in 1..10, in 19..28 -> if (number % 2 == 0) Color.Black else Color.Red
        in 11..18, in 29..36 -> if (number % 2 == 0) Color.Red else Color.Black
        else -> Color.Gray
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRouletteGameScreen() {
    val dummyViewModel = RouletteViewModel()
    val navController = rememberNavController()
    RouletteGameScreen(viewModel = dummyViewModel, navController = navController)
}