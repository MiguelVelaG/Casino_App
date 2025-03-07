package com.miguel.casinoapp.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.miguel.casinoapp.R
import com.miguel.casinoapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class) // Añade esta anotación
@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF000000), Color(0xFFB8860B)), // Gradiente negro a dorado (luxuoso)
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Texto "Casino App" encima de la imagen
        Text(
            text = "Casino App",
            style = MaterialTheme.typography.headlineLarge.copy(color = Color.Yellow),
            modifier = Modifier.padding(bottom = 16.dp) // Espacio entre el texto y la imagen
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Logo de la app
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(270.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de correo electrónico
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("Correo electrónico") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color(0xFF1EAB65),
                unfocusedIndicatorColor = Color(0xFFB0B0B0),
                focusedLabelColor = Color(0xFF1EAB65),
                unfocusedLabelColor = Color(0xFFB0B0B0),
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 16.dp) // Agrega margen a los lados
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color(0xFF1EAB65),
                unfocusedIndicatorColor = Color(0xFFB0B0B0),
                focusedLabelColor = Color(0xFF1EAB65),
                unfocusedLabelColor = Color(0xFFB0B0B0),
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de inicio de sesión
        Button(
            onClick = {
                authViewModel.loginWithEmail(email.value, password.value) {
                    navController.navigate("mainMenu") {
                        popUpTo("login") { inclusive = true } // Borra el login del historial
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A), contentColor = Color.White),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(50.dp), // Botón grande y destacado
            shape = CircleShape,
            contentPadding = PaddingValues(vertical = 12.dp),
        ) {
            Text("Iniciar Sesión", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de registro para nuevos usuarios
        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta? Regístrate aquí", color = Color.White)
        }
    }
}
