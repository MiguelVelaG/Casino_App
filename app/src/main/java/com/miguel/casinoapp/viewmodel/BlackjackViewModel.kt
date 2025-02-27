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
    val errorMessage: String? = null
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
            _state.value = BlackjackState()
            val response = RetrofitInstance.api.shuffleDeck()
            if (response.isSuccessful) {
                response.body()?.let { deckResponse ->
                    _state.value = _state.value.copy(deckId = deckResponse.deckId)
                }
            } else {
                _state.value = _state.value.copy(errorMessage = "Error al obtener el mazo.")
            }
        }
    }

    fun playerHit() {
        Log.d("Blackjack", "Pedir carta presionado")
        viewModelScope.launch {
            val deckId = _state.value.deckId ?: return@launch
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
                gameResult = gameResult
            )
        }
    }

    private suspend fun drawCards(deckId: String, count: Int): List<Card> {
        val response = RetrofitInstance.api.drawCards(deckId, count)
        return if (response.isSuccessful) {
            response.body()?.cards?.map { cardResponse ->
                Card(
                    value = cardResponse.value,
                    suit = cardResponse.suit,
                    imageUrl = cardResponse.imageUrl
                )
            } ?: emptyList()
        } else {
            _state.value = _state.value.copy(errorMessage = "Error al dibujar las cartas.")
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
