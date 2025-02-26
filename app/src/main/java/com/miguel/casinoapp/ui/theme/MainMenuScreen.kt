package com.miguel.casinoapp.ui.theme

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.miguel.casinoapp.R

@Composable
fun MainMenuScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF388E3C),
                        Color(0xFFD32F2F)
                    ), // Gradiente de verde a rojo
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY)
                )
            ) // Fondo con gradiente
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título con sombra suave y ajustada
            Text(
                text = "Bienvenido al Casino",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700), // Color dorado brillante
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .shadow(
                        elevation = 4.dp, // Sombra más suave
                        shape = RoundedCornerShape(8.dp),
                        clip = false, // No recortar la sombra
                        ambientColor = Color(0x55000000), // Sombra suave y sutil
                        spotColor = Color(0x33000000) // Sombra más ligera en el punto
                    )
            )

            // Botón de "Juego de Dados"
            CustomButton(
                text = "Juego de Dados",
                onClick = {
                    Log.d(
                        "MainMenuScreen",
                        "Navegando al Dice..."
                    ) // Log justo antes de la navegación
                    navController.navigate("Dice")
                },
                iconRes = R.drawable.ic_dice, // Asegúrate de tener este icono
                buttonColor = Color(0xFF388E3C), // Verde para el botón
                iconColor = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de "Ruleta"
            CustomButton(
                text = "Ruleta",
                onClick = {
                    Log.d("MainMenuScreen", "Navegando a la ruleta...") // Log justo antes de la navegación
                    navController.navigate("roulette")
                },
                iconRes = R.drawable.ic_roulette,
                buttonColor = Color(0xFFD32F2F), // Rojo para el botón de ruleta
                iconColor = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de "Blackjack"
            CustomButton(
                text = "Blackjack",
                onClick = {
                    Log.d("MainMenuScreen", "Navegando al Blackjack...") // Log justo antes de la navegación
                    navController.navigate("blackjack")
                },
                iconRes = R.drawable.ic_blackjack,
                buttonColor = Color(0xFFFFA000), // Naranja dorado para blackjack
                iconColor = Color.White
            )
        }
    }
}
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    iconRes: Int,
    buttonColor: Color,
    iconColor: Color
) {
    ElevatedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 32.dp)
            .animateContentSize(), // Animación para el cambio de tamaño
        shape = RoundedCornerShape(24.dp), // Bordes redondeados
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.elevatedButtonElevation(2.dp), // Sombra mínima para profesionalismo
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp) // Tamaño del icono
                    .animateContentSize(), // Animación de cambio de tamaño
                tint = iconColor // Cambia el color del icono
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                style = androidx.compose.ui.text.TextStyle(
                    letterSpacing = 1.5.sp // Agregar un poco de espaciado entre letras para un toque más elegante
                )
            )
        }
    }
}
