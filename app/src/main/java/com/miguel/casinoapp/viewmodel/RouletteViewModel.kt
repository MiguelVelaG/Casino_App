package com.miguel.casinoapp.viewmodel

import android.media.MediaPlayer
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class RouletteViewModel : ViewModel() {
    private val _coins = mutableIntStateOf(10000)
    val coins: State<Int> = _coins

    private val _betAmount = mutableIntStateOf(0)
    val betAmount: State<Int> = _betAmount

    private val _selectedBet = mutableStateOf("")
    val selectedBet: State<String> = _selectedBet

    private val _betResult = mutableStateOf("")
    val betResult: State<String> = _betResult

    private val _selectedNumber = mutableStateOf<Int?>(null) // Estado para número seleccionado
    val selectedNumber: State<Int?> = _selectedNumber

    private val _rotationAngle = mutableFloatStateOf(0f)
    val rotationAngle: State<Float> get() = _rotationAngle

    private val _ballPosition = mutableFloatStateOf(0f)
    val ballPosition: State<Float> get() = _ballPosition

    private val rouletteNumbers = listOf(
        0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26, 0
    )

    fun updateRotationAngle(newAngle: Float) {
        _rotationAngle.floatValue = newAngle
    }

    fun updateBallPosition(newPosition: Float) {
        _ballPosition.floatValue = newPosition
    }

    private fun updateCoins(newCoins: Int) {
        _coins.intValue = newCoins
    }

    fun updateBetAmount(newAmount: Int) {
        _betAmount.intValue = newAmount
    }

    fun updateSelectedBet(newBet: String) {
        _selectedBet.value = newBet
    }

    private fun updateBetResult(newResult: String) {
        _betResult.value = newResult
    }

    fun updateSelectedNumber(newNumber: Int?) {
        _selectedNumber.value = newNumber // Actualiza el número seleccionado
    }

    // Método para hacer girar la ruleta
    fun spinRoulette(
        coins: Int,
        betAmount: Int,
        selectedBet: String,
        mediaPlayer: MediaPlayer
    ) {
        if (betAmount in 1..coins) {
            mediaPlayer.start()

            // Generar un ángulo aleatorio para la ruleta (mínimo 3 giros completos + posición final)
            val randomAngle = (360 * 3 + (0..360).random()).toFloat()
            _rotationAngle.floatValue = randomAngle

            // Generar la posición de la bola dentro del rango de 0 a 360 grados
            val randomBallPosition = (0..360).random().toFloat()
            _ballPosition.floatValue = randomBallPosition

            // Normalizar la posición de la bola
            val normalizedBallPosition = (randomBallPosition % 360 + 360) % 360

            // Calcular el número ganador basado en la posición de la bola
            val segmentSize = 360f / rouletteNumbers.size
            val ballSegmentIndex = ((normalizedBallPosition / segmentSize).toInt()) % rouletteNumbers.size
            val winningNumber = rouletteNumbers[ballSegmentIndex]

            // Determinar el color del número ganador
            val rollColor = getColorForNumber(winningNumber)

            // Verificar el resultado de la apuesta
            val winnings = when {
                selectedNumber.value != null && winningNumber == selectedNumber.value -> betAmount * 36 // Apuesta numérica
                winningNumber.toString() == selectedBet -> betAmount * 36 // Apuesta textual (número como texto)
                selectedBet == "rojo" && rollColor == Color.Red -> betAmount * 2 // Rojo
                selectedBet == "negro" && rollColor == Color.Black -> betAmount * 2 // Negro
                selectedBet == "verde" && winningNumber == 0 -> betAmount * 36 // Verde
                else -> 0
            }

            // Actualizar estado de monedas y resultado
            updateCoins(if (winnings > 0) coins + winnings else coins - betAmount)
            updateBetResult(
                if (winnings > 0) "¡Ganaste! $winnings monedas. Número ganador: $winningNumber"
                else "Perdiste $betAmount monedas. Número ganador: $winningNumber"
            )
            updateBetAmount(0) // Reinicia la apuesta
        } else {
            updateBetResult("Apuesta no válida.")
        }
    }

    // Función que determina el color del número (verde, rojo, negro)
    private fun getColorForNumber(number: Int): Color {
        return when (number) {
            0 -> Color.Green
            in 1..10, in 19..28 -> if (number % 2 == 0) Color.Black else Color.Red
            in 11..18, in 29..36 -> if (number % 2 == 0) Color.Red else Color.Black
            else -> Color.Gray
        }
    }
}
