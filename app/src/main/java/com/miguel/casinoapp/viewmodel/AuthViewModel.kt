package com.miguel.casinoapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    var user by mutableStateOf(auth.currentUser)
        private set

    var profilePicture by mutableStateOf<String?>(null)
        private set

    var balance by mutableDoubleStateOf(0.0)
        private set

    var nickname by mutableStateOf<String?>(null) // ✅ Se agrega la variable de estado
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    // Cargar datos del usuario desde Firestore
    fun getUserData() {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            errorMessage = "Usuario no autenticado"
            return
        }

        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    profilePicture = document.getString("profilePicture") ?: "default"
                    balance = document.getDouble("balance") ?: 0.0
                    nickname = document.getString("nickname")

                    Log.d("Firestore", "Imagen obtenida: $profilePicture")
                } else {
                    errorMessage = "El usuario no tiene datos en Firestore"
                    Log.e("Firestore", "No hay datos en Firestore para este usuario")
                }
            }
            .addOnFailureListener { e ->
                errorMessage = "Error al obtener datos: ${e.localizedMessage}"
                Log.e("Firestore", "Error obteniendo datos del usuario", e)
            }
    }



    // ✅ Actualizar el nickname del usuario en Firestore
    fun updateUserNickname(newNickname: String) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .update("nickname", newNickname)
            .addOnSuccessListener {
                nickname = newNickname // Actualizar en el estado
                Log.d("Firestore", "Nickname actualizado")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error actualizando nickname", e)
            }
    }



    // ✅ Eliminar cuenta del usuario
    fun deleteUserAccount(navController: NavHostController) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).delete()
            .addOnSuccessListener {
                auth.currentUser?.delete()?.addOnSuccessListener {
                    logout()
                    navController.navigate("login") {
                        popUpTo("mainMenu") { inclusive = true }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error eliminando cuenta", e)
            }
    }

    // Iniciar sesión con email y contraseña
    fun loginWithEmail(email: String, password: String, onSuccess: () -> Unit) {
        errorMessage = null
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user = auth.currentUser
                    getUserData() // Llamar a getUserData() después de iniciar sesión
                    onSuccess()
                } else {
                    errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "Usuario no registrado"
                        is FirebaseAuthInvalidCredentialsException -> "Contraseña incorrecta"
                        else -> "Error al iniciar sesión"
                    }
                }
            }
    }

    // Registrar un usuario con email y contraseña
    fun registerWithEmail(email: String, password: String, onSuccess: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userData = hashMapOf(
                        "email" to email,
                        "balance" to 10000.0, // Empieza con 10,000 monedas
                        "nickname" to "Nuevo Jugador"
                    )

                    firestore.collection("users").document(userId)
                        .set(userData)
                        .addOnSuccessListener {
                            Log.d("Firestore", "Usuario registrado con 10,000 monedas")
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error guardando datos del usuario", e)
                        }
                } else {
                    errorMessage = task.exception?.message
                }
            }
    }


    private fun saveUserToFirestore(userId: String, email: String) {
        val userData = hashMapOf(
            "email" to email,
            "profilePicture" to "https://example.com/default-avatar.png", // Imagen por defecto
            "balance" to 100.0, // Saldo inicial
            "nickname" to "Nuevo Usuario" // ✅ Nickname por defecto
        )

        firestore.collection("users").document(userId)
            .set(userData)
            .addOnSuccessListener {
                Log.d("Firestore", "Usuario registrado en Firestore correctamente")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error al guardar datos del usuario", e)
            }
    }


    // Cerrar sesión
    fun logout() {
        auth.signOut()
        user = null
        profilePicture = null
        balance = 0.0
        nickname = null
    }
}
