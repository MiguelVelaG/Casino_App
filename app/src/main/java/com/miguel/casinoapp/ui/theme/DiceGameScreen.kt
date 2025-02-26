package com.miguel.casinoapp.ui.theme


import android.content.res.Configuration
import android.media.MediaPlayer
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.miguel.casinoapp.R
import com.miguel.casinoapp.R.drawable.ic_home
import com.miguel.casinoapp.R.raw.dice_roll2
import com.miguel.casinoapp.viewmodel.DiceRollerViewModel


@Composable
fun DiceGameScreen(viewModel: DiceRollerViewModel, navController: NavHostController) {
    // Aquí pasamos directamente las variables, sin declarar su tipo de nuevo
    DiceWithButtonAndImage(viewModel = viewModel, navController = navController)
}


@Composable
fun DiceWithButtonAndImage(viewModel: DiceRollerViewModel, navController: NavHostController) {
    // Detecta la orientación del dispositivo
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var result by remember { mutableIntStateOf(1) }
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    val rotation by animateFloatAsState(targetValue = rotationAngle, animationSpec = tween(durationMillis = 1000),
        label = ""
    )

    // Datos del ViewModel
    val coins by viewModel.coins
    val betAmount by viewModel.betAmount
    val selectedSide by viewModel.selectedSide
    val betResult by viewModel.betResult

    //Audio del dado
    val mediaPlayer = MediaPlayer.create(LocalContext.current, dice_roll2)

    // Estadísticas de los dados
    var stats by remember { mutableStateOf(mapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0)) }
    val totalRolls = stats.values.sum()
    val percentageStats = stats.mapValues {
        if (totalRolls > 0) (it.value * 100) / totalRolls else 0
    }

    // Fondo simula una mesa de dados
    val tableBackground = Color(0xFF006400)
    val woodColor = Color(0xFF8B4513)

    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    // UI principal, dependiendo de la orientación
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(tableBackground)
            .border(8.dp, woodColor)
            .padding(10.dp)
    ) {
        // Botón para regresar al menú principal (solo una imagen ahora)
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

        // Usamos Column o Row dependiendo de la orientación
        if (isLandscape) {
            // Vista horizontal con el dado grande y estadísticas resumidas
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Top
            ) {
                // Columna izquierda: Dado grande
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp) // Menos espaciado entre los elementos
                ) {
                    // 1. Espacio para bajar el dado
                    Spacer(modifier = Modifier.height(40.dp)) // Baja el dado un poco

                    // 2. Dado grande
                    Image(
                        painter = painterResource(id = imageResource),
                        contentDescription = "Dado",
                        modifier = Modifier
                            .size(250.dp) // Aumentamos el tamaño del dado
                            .rotate(rotation)
                    )

                    // 3. Mostrar el resultado de la apuesta debajo del dado
                    Text(
                        betResult, // El texto del resultado (acertaste o fallaste)
                        fontSize = 18.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp) // Menos espacio entre el dado y el resultado
                    )

                    // 4. Estadísticas distribuidas en dos filas
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center, // Alinear al centro
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mostramos estadísticas de las caras 1, 2, 3 en una fila
                        percentageStats.filter { it.key in 1..3 }.forEach { (side, percentage) ->
                            Text(
                                text = "Cara $side: $percentage%",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(4.dp) // Menos espaciado entre estadísticas
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(0.dp)) // Menos separación entre las dos filas

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center, // Alinear al centro
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Mostramos estadísticas de las caras 4, 5, 6 en la segunda fila
                        percentageStats.filter { it.key in 4..6 }.forEach { (side, percentage) ->
                            Text(
                                text = "Cara $side: $percentage%",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(4.dp) // Menos espaciado entre estadísticas
                            )
                        }
                    }
                }

                // Columna derecha: Monedas, Lado a apostar, Apuesta y botón para tirar
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp) // Espaciado mayor
                ) {
                    // 1. Mostrar monedas
                    Text(
                        "Monedas: $coins",
                        fontSize = 30.sp,
                        color = Color.Yellow,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 40.dp) // Más espacio para no quedar pegado a la parte superior
                    )

                    // 2. Mostrar botones para seleccionar el lado a apostar
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (i in 1..6) {
                            Button(
                                onClick = { viewModel.updateSelectedSide(i) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedSide == i) Color(0xFF1E3A8A) else Color.Gray
                                ),
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Text(text = i.toString(), color = Color.White, fontSize = 18.sp)
                            }
                        }
                    }

                    // 3. Mostrar cantidad apostada
                    Text(
                        "Cantidad a apostar: $betAmount",
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    // 4. Ajuste de la apuesta
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

                    // 5. Botón para lanzar el dado
                    Button(
                        onClick = {
                            if (betAmount > 0) {
                                // Reproducir el sonido
                                mediaPlayer.start()

                                val newRoll = (1..6).random()
                                result = newRoll
                                rotationAngle += 720f

                                // Actualizamos las estadísticas de los dados
                                stats = stats.toMutableMap().apply {
                                    put(newRoll, getValue(newRoll) + 1)
                                }

                                // Lógica para actualizar monedas
                                if (newRoll == selectedSide) {
                                    val winnings = betAmount * 6
                                    viewModel.updateCoins(coins + winnings)
                                    viewModel.updateBetResult("¡Ganaste! $winnings monedas.")
                                } else {
                                    viewModel.updateCoins(coins - betAmount)
                                    viewModel.updateBetResult("Perdiste! $betAmount monedas.")
                                }
                                viewModel.updateBetAmount(0)
                            } else {
                                viewModel.updateBetResult("¡Debes apostar algo!")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A), contentColor = Color.White),
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Text("Tirar Dado", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }

                }
            }
        } else {
            // Aquí va la parte de la vista vertical (no cambiamos nada de la estructura vertical)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp)) // Espacio superior
                Text("Monedas: $coins", fontSize = 30.sp, color = Color.Yellow, fontWeight = FontWeight.Bold)

                // Mostrar botones para seleccionar la cara
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (i in 1..6) {
                        Button(
                            onClick = { viewModel.updateSelectedSide(i) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedSide == i) Color(0xFF1E3A8A) else Color.Gray
                            ),
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text(text = i.toString(), color = Color.White, fontSize = 18.sp)
                        }
                    }
                }

                // Mostrar cantidad a apostar
                Text(
                    "Cantidad a apostar: $betAmount",
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                // Ajuste de apuesta
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

                // Mostrar el dado
                Image(
                    painter = painterResource(id = imageResource),
                    contentDescription = "Dado",
                    modifier = Modifier
                        .size(150.dp)
                        .rotate(rotation)
                )

                // Botón para lanzar el dado
                Button(
                    onClick = {
                        if (betAmount > 0) {
                            // Reproducir el sonido
                            mediaPlayer.start()

                            val newRoll = (1..6).random()
                            result = newRoll
                            rotationAngle += 720f

                            // Actualizamos las estadísticas de los dados
                            stats = stats.toMutableMap().apply {
                                put(newRoll, getValue(newRoll) + 1)
                            }

                            // Lógica para actualizar monedas
                            if (newRoll == selectedSide) {
                                val winnings = betAmount * 6
                                viewModel.updateCoins(coins + winnings)
                                viewModel.updateBetResult("¡Ganaste! $winnings monedas.")
                            } else {
                                viewModel.updateCoins(coins - betAmount)
                                viewModel.updateBetResult("Perdiste! $betAmount monedas.")
                            }
                            viewModel.updateBetAmount(0)
                        } else {
                            viewModel.updateBetResult("¡Debes apostar algo!")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A), contentColor = Color.White),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text("Tirar Dado", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }

                // Mostrar el resultado
                Text(
                    betResult,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                // Mostrar estadísticas
                Text("Estadísticas:", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
                stats.forEach { (side, count) ->
                    val percentage = percentageStats[side] ?: 0
                    Text(
                        "Cara $side: $count veces (${percentage}% de las tiradas)",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }

}




