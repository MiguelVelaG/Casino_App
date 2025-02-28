package com.miguel.casinoapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguel.casinoapp.data.Card
import com.miguel.casinoapp.network.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class BlackjackState(
    val playerHand: List<Card> = emptyList(),
    val dealerHand: List<Card> = emptyList(),
    val playerScore: Int = 0,
    val dealerScore: Int = 0,
    val isPlayerTurn: Boolean = true,
    val gameResult: String? = null,
    val deckId: String? = null,
    val errorMessage: String? = null,
    val isGameOver: Boolean = false // Nueva propiedad para saber si el juego terminó
)


class BlackjackViewModel : ViewModel() {
    private val _state = MutableStateFlow(BlackjackState())
    val state: StateFlow<BlackjackState> get() = _state

    init {
        resetGame()
    }

    fun resetGame() {
        Log.d("Blackjack", "Reiniciando juego")
        viewModelScope.launch {
            // Asegurarse de que siempre se reinicie el mazo
            _state.value = BlackjackState() // Reiniciar el estado de juego

            // Realizar la llamada para obtener un nuevo mazo
            val response = RetrofitInstance.api.shuffleDeck()

            Log.d("Blackjack", "Código de respuesta: ${response.code()}")
            Log.d("Blackjack", "Cuerpo de la respuesta: ${response.body()}")

            if (response.isSuccessful) {
                response.body()?.let { deckResponse ->
                    Log.d("Blackjack", "Nuevo deckId recibido: ${deckResponse.deckId}, Cartas restantes: ${deckResponse.remaining}")
                    _state.value = _state.value.copy(deckId = deckResponse.deckId)
                }
            } else {
                Log.e("Blackjack", "Error al obtener el mazo: ${response.message()}")
                _state.value = _state.value.copy(errorMessage = "Error al obtener el mazo.")
            }
        }
    }

    fun playerHit() {
        Log.d("Blackjack", "Pedir carta presionado")
        viewModelScope.launch {
            val deckId = _state.value.deckId
            Log.d("Blackjack", "Deck ID actual antes de robar carta: $deckId")

            if (deckId == null) {
                Log.e("Blackjack", "Error: deckId es null")
                _state.value = _state.value.copy(errorMessage = "El mazo no está disponible.")
                return@launch
            }

            try {
                // Intentamos robar la carta
                val drawnCards = drawCards(deckId, 1)
                drawnCards.firstOrNull()?.also { newCard ->
                    val newHand = _state.value.playerHand + newCard
                    val newScore = calculateScore(newHand)

                    _state.value = _state.value.copy(
                        playerHand = newHand,
                        playerScore = newScore,
                        isPlayerTurn = newScore <= 21,
                        gameResult = if (newScore > 21) "Lose" else _state.value.gameResult
                    )
                    Log.d("Blackjack", "Nueva mano del jugador: $newHand, Puntos: $newScore")
                }
            } catch (e: Exception) {
                Log.e("Blackjack", "Error al pedir carta: ${e.message}")
                _state.value = _state.value.copy(errorMessage = "Error al pedir carta.")
            }
        }
    }

    fun playerStand() {
        Log.d("Blackjack", "El jugador se planta")
        viewModelScope.launch {
            val deckId = _state.value.deckId ?: return@launch
            var newDealerHand = _state.value.dealerHand
            var newDealerScore = calculateScore(newDealerHand)

            while (newDealerScore < 17) {
                drawCards(deckId, 1).firstOrNull()?.also { newCard ->
                    newDealerHand = newDealerHand + newCard
                    newDealerScore = calculateScore(newDealerHand)

                    _state.value = _state.value.copy(dealerHand = newDealerHand, dealerScore = newDealerScore)

                    delay(1000)
                } ?: break
            }

            val gameResult = when {
                newDealerScore > 21 -> "Win"
                newDealerScore > _state.value.playerScore -> "Lose"
                newDealerScore < _state.value.playerScore -> "Win"
                else -> "Draw"
            }

            _state.value = _state.value.copy(
                isPlayerTurn = false,
                gameResult = gameResult,
                isGameOver = true // Establecer que el juego ha terminado
            )
        }
    }

    private suspend fun drawCards(deckId: String, count: Int): List<Card> {
        Log.d("Blackjack", "Intentando robar $count carta(s) del mazo $deckId")

        return try {
            val response = RetrofitInstance.api.drawCards(deckId, count)

            Log.d("Blackjack", "Código de respuesta: ${response.code()}")
            Log.d("Blackjack", "Cuerpo de la respuesta: ${response.body()}")

            if (response.isSuccessful) {
                val cards = response.body()?.cards ?: emptyList()

                cards.forEach { card ->
                    Log.d("Blackjack", "Carta obtenida: ${card.value} de ${card.suit}")
                }

                cards.map { cardResponse ->
                    Log.d("Blackjack", "URL de la imagen: ${cardResponse.image}")

                    // Si la URL de la imagen es null, se asigna una imagen de error o carta dada vuelta
                    val imageUrl = cardResponse.image ?: "https://example.com/carta_dada_vuelta.png" // URL de la carta dada vuelta

                    Card(
                        value = cardResponse.value,
                        suit = cardResponse.suit,
                        image = imageUrl
                    )
                }
            } else {
                Log.e("Blackjack", "Error al robar cartas: ${response.message()}")
                _state.value = _state.value.copy(errorMessage = "Error al robar cartas.")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("Blackjack", "Error de red o al procesar la respuesta: ${e.message}")
            _state.value = _state.value.copy(errorMessage = "Error al robar cartas.")
            emptyList()
        }
    }

    private fun parseCardValue(value: String): Int = when (value) {
        "ACE" -> 11
        "KING", "QUEEN", "JACK" -> 10
        else -> value.toIntOrNull() ?: 0
    }

    private fun calculateScore(hand: List<Card>): Int {
        var score = hand.sumOf { parseCardValue(it.value) }
        var aceCount = hand.count { it.value == "ACE" }

        while (score > 21 && aceCount > 0) {
            score -= 10
            aceCount--
        }
        return score
    }
}
