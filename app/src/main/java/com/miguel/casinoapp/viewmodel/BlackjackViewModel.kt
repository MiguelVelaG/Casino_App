package com.miguel.casinoapp.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguel.casinoapp.data.Card
import com.miguel.casinoapp.data.DeckResponse
import com.miguel.casinoapp.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class BlackjackState(
    val playerHand: List<Card> = emptyList(),
    val dealerHand: List<Card> = emptyList(),
    val playerScore: Int = 0,
    val dealerScore: Int = 0,
    val isPlayerTurn: Boolean = true,
    val gameResult: String? = null, // "Win", "Lose", "Draw"
    val deckId: String = "" // ID de la baraja generada por la API
)

class BlackjackViewModel : ViewModel() {
    private val _state = MutableStateFlow(BlackjackState())
    val state: StateFlow<BlackjackState> get() = _state

    init {
        resetGame()
    }

    fun resetGame() {
        viewModelScope.launch {
            // Generar un nuevo deck usando la API
            val response = RetrofitInstance.api.shuffleDeck()
            if (response.isSuccessful) {
                response.body()?.let { deckResponse: DeckResponse -> // Especificar el tipo explícitamente
                    val deckId = deckResponse.deckId

                    // Robar las cartas iniciales dentro de la misma corrutina
                    val initialPlayerHand = this@BlackjackViewModel.drawCards(
                        deckId = deckId,
                        count = 2
                    )
                    val initialDealerHand = this@BlackjackViewModel.drawCards(
                        deckId = deckId,
                        count = 1
                    )

                    // Asignar el nuevo estado a la vista después de obtener las cartas
                    _state.value = BlackjackState(
                        playerHand = initialPlayerHand,
                        dealerHand = initialDealerHand,
                        playerScore = calculateScore(initialPlayerHand),
                        dealerScore = calculateScore(initialDealerHand),
                        isPlayerTurn = true,
                        gameResult = null,
                        deckId = deckId
                    )
                }
            }
        }
    }


    fun playerHit() {
        if (_state.value.isPlayerTurn) {
            viewModelScope.launch {
                val deckId = _state.value.deckId
                val newCard = drawCards(deckId, 1).firstOrNull()
                if (newCard != null) {
                    val newHand = _state.value.playerHand + newCard
                    val newScore = calculateScore(newHand)

                    if (newScore > 21) {
                        _state.value = _state.value.copy(
                            playerHand = newHand,
                            playerScore = newScore,
                            isPlayerTurn = false,
                            gameResult = "Lose"
                        )
                    } else {
                        _state.value = _state.value.copy(
                            playerHand = newHand,
                            playerScore = newScore
                        )
                    }
                }
            }
        }
    }

    fun playerStand() {
        _state.value = _state.value.copy(isPlayerTurn = false)
        dealerTurn()
    }

    private fun dealerTurn() {
        viewModelScope.launch {
            val deckId = _state.value.deckId

            while (_state.value.dealerScore < 17) {
                val newCard = drawCards(deckId, 1).firstOrNull()
                if (newCard != null) {
                    val newHand = _state.value.dealerHand + newCard
                    val newScore = calculateScore(newHand)

                    _state.value = _state.value.copy(
                        dealerHand = newHand,
                        dealerScore = newScore
                    )
                }
            }

            evaluateWinner()
        }
    }

    private fun evaluateWinner() {
        val playerScore = _state.value.playerScore
        val dealerScore = _state.value.dealerScore

        val result = when {
            dealerScore > 21 || playerScore > dealerScore -> "Win"
            playerScore == dealerScore -> "Draw"
            else -> "Lose"
        }

        _state.value = _state.value.copy(gameResult = result)
    }

    private suspend fun drawCards(deckId: String, count: Int): List<Card> {
        val response = RetrofitInstance.api.drawCards(deckId, count)
        return if (response.isSuccessful) {
            response.body()?.cards?.map {
                Card(
                    value = it.value,
                    suit = it.suit,
                    imageUrl = it.imageUrl
                )
            } ?: emptyList()
        } else {
            // Manejar el error aquí
            println("Error al obtener cartas: ${response.message()}")
            emptyList()
        }
    }



    private fun parseCardValue(value: String): Int {
        return when (value) {
            "ACE" -> 11
            "KING", "QUEEN", "JACK" -> 10
            else -> value.toIntOrNull() ?: 0
        }
    }

    private fun calculateScore(hand: List<Card>): Int {
        val score = hand.sumOf { parseCardValue(it.value) }
        val aceCount = hand.count { it.value == "ACE" }

        return if (score > 21 && aceCount > 0) {
            score - (10 * aceCount.coerceAtMost((score - 21) / 10))
        } else {
            score
        }
    }
}

