package com.miguel.casinoapp.ui.theme


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.miguel.casinoapp.R
import com.miguel.casinoapp.R.drawable.ic_home
import com.miguel.casinoapp.viewmodel.AuthViewModel

@Composable
fun UserProfileScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    var nickname by remember { mutableStateOf(authViewModel.nickname ?: "") }
    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(authViewModel.user) {
        authViewModel.getUserData()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.White, Color(0xFFB8860B))
                )
            )
    ) {IconButton(
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Imagen de perfil
            Image(
                painter = painterResource(id = R.drawable.profile_default),
                contentDescription = "Perfil de usuario",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .clickable { navController.navigate("userProfile") }, // Navega a perfil
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar email del usuario (no editable)
            Text("Correo: ${authViewModel.user?.email ?: "No disponible"}", fontSize = 18.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar saldo del usuario
            Text("Saldo: $${"%.2f".format(authViewModel.balance)}", fontSize = 18.sp, color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

            // Campo para editar nickname
            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                label = { Text("Nickname", color = Color.White) },
                enabled = isEditing,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color(0xFF1E3A8A),
                    unfocusedContainerColor = Color(0xFF3F3F3F),
                    focusedIndicatorColor = Color.Yellow,
                    unfocusedIndicatorColor = Color.White,
                    focusedLabelColor = Color.Yellow,
                    unfocusedLabelColor = Color.White,
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para editar o guardar nickname
            Button(
                onClick = {
                    if (isEditing) {
                        authViewModel.updateUserNickname(nickname)
                    }
                    isEditing = !isEditing
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text(if (isEditing) "Guardar" else "Editar Nickname")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para cerrar sesión
            Button(
                onClick = {
                    authViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("mainMenu") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Cerrar Sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para eliminar cuenta
            Button(
                onClick = {
                    authViewModel.deleteUserAccount(navController)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text("Eliminar Cuenta")
            }
        }
    }
}
