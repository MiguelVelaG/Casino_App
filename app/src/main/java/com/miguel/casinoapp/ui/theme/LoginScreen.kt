package com.miguel.casinoapp.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.miguel.casinoapp.R
import com.miguel.casinoapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Black, Color(0xFFB8860B))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Casino App",
            style = MaterialTheme.typography.headlineLarge.copy(color = Color.Yellow),
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
            value = email,
            onValueChange = { email = it },
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
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
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

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Los campos no pueden estar vacíos"
                } else {
                    authViewModel.loginWithEmail(email, password) {
                        if (authViewModel.user != null) {
                            navController.navigate("mainMenu") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            errorMessage = "Usuario no registrado o contraseña incorrecta"
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Iniciar sesión", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "¿No tienes cuenta? Regístrate aquí",
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.clickable {
                navController.navigate("register")
            }
        )
    }

    // Muestra el cuadro de diálogo si hay un error
    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            confirmButton = {
                Button(
                    onClick = { errorMessage = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)
                ) {
                    Text("OK", color = Color.Black)
                }
            },
            title = { Text("Error", color = Color.Black) },
            text = { Text(errorMessage ?: "", color = Color.Black) },
            containerColor = Color.White
        )
    }
}

