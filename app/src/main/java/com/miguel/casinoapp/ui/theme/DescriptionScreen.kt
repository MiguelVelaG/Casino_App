package com.miguel.casinoapp.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.miguel.casinoapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun DescriptionScreen(navController: NavController) {
    var inputText by remember { mutableStateOf("") }
    var isSending by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedReason by remember { mutableStateOf("Sugerencia") }
    var wantsResponse by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val faqList = listOf(
        "¿Cómo puedo jugar a los dados?",
        "¿Cómo funciona la ruleta?",
        "¿Es posible retirar ganancias?",
        "¿Puedo jugar sin conexión?"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF03A9F4),
                            Color(0xFFB1E4EC)
                        ), // Gradiente de verde a rojo
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY)
                    )
                ) // Fondo con gradiente
                .padding(16.dp)
        ) {
            // Botón de regreso
            IconButton(
                onClick = {
                    navController.navigate("mainMenu") {
                        popUpTo("mainMenu") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 25.dp, start = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_home),
                    contentDescription = "Volver al menú",
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Imagen de la app
            Image(
                painter = painterResource(id = R.drawable.logo_dado),
                contentDescription = "Casino",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(bottom = 16.dp)
            )

            // Tarjeta de información
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bienvenido al Casino App",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Disfruta de juegos de azar como dados, ruleta y blackjack. ¡Diviértete y gana en grande!",
                        style = TextStyle(
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // RadioButtons para elegir motivo de contacto
                    Text("Motivo del mensaje:")
                    Row {
                        listOf("Sugerencia", "Problema").forEach { reason ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                RadioButton(
                                    selected = selectedReason == reason,
                                    onClick = { selectedReason = reason }
                                )
                                Text(reason)
                            }
                        }
                    }

                    // Checkbox para solicitar respuesta
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = wantsResponse,
                            onCheckedChange = { wantsResponse = it }
                        )
                        Text("Deseo recibir respuesta")
                    }

                    // TextField
                    BasicTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(Color.Gray.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                            .padding(16.dp),
                        textStyle = TextStyle(fontSize = 16.sp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón para enviar mensaje
                    ElevatedButton(
                        onClick = {
                            isSending = true
                            scope.launch {
                                delay(2000)
                                isSending = false
                                showDialog = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Enviar mensaje")
                    }

                    // ProgressBar
                    if (isSending) {
                        CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de preguntas frecuentes
            Text("Preguntas Frecuentes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(faqList) { question ->
                    Text(text = "- $question", modifier = Modifier.padding(8.dp))
                }
            }
        }
    }

    // Diálogo de confirmación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Mensaje enviado") },
            text = { Text("Tu mensaje ha sido enviado correctamente. Gracias por tu feedback!") },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}
