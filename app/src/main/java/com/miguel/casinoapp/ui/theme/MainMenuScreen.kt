package com.miguel.casinoapp.ui.theme

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.miguel.casinoapp.R
import com.miguel.casinoapp.viewmodel.AuthViewModel

@Composable
fun MainMenuScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val firestore = FirebaseFirestore.getInstance()
    var userBalance by remember { mutableDoubleStateOf(10_000.0) } // Inicialmente 10,000 monedas

    // Cargar saldo del usuario al iniciar la pantalla
    LaunchedEffect(authViewModel.user?.uid) {
        authViewModel.user?.uid?.let { userId ->
            firestore.collection("users").document(userId)
                .addSnapshotListener { document, error ->
                    if (error == null && document != null) {
                        userBalance = document.getDouble("balance") ?: 10000.0
                    }
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF388E3C), Color(0xFFD32F2F)) // Verde a Rojo
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nueva Sección: Imagen de perfil y saldo alineados arriba
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, bottom = 40.dp), // 🔹 Aumentado el bottom padding para bajar los botones
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_default),
                    contentDescription = "Perfil de usuario",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .clickable { navController.navigate("userProfile") }, // Ir a perfil
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(20.dp))

                Text(
                    text = "💰 ${"%,d".format(userBalance.toInt())} monedas",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(180.dp)) // 🔹 Espacio adicional para bajar más los botones

            // Botones de navegación a los juegos
            CustomButton("Juego de Dados", R.drawable.ic_dice, Color(0xFF388E3C)) {
                navController.navigate("Dice") { popUpTo("mainMenu") { inclusive = false } }
            }

            Spacer(modifier = Modifier.height(24.dp)) // 🔹 Aumenté la separación entre botones

            CustomButton("Ruleta", R.drawable.ic_roulette, Color(0xFFD32F2F)) {
                navController.navigate("roulette") { popUpTo("mainMenu") { inclusive = false } }
            }

            Spacer(modifier = Modifier.height(24.dp))

            CustomButton("Blackjack", R.drawable.ic_blackjack, Color(0xFFFFA000)) {
                navController.navigate("blackjack") { popUpTo("mainMenu") { inclusive = false } }
            }

            Spacer(modifier = Modifier.height(24.dp))

            CustomButton("Descripción de la App", R.drawable.info, Color(0xFF3F51B5)) {
                navController.navigate("description") { popUpTo("mainMenu") { inclusive = false } }
            }
        }
    }
}

// Componente de Botón Reutilizable
@Composable
fun CustomButton(text: String, iconRes: Int, buttonColor: Color, onClick: () -> Unit) {
    ElevatedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(64.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor, contentColor = Color.White),
        elevation = ButtonDefaults.elevatedButtonElevation(2.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}
