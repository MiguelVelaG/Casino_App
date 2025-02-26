package com.miguel.casinoapp.data

data class DeckResponse(
    val deckId: String,
    val remaining: Int,
    val shuffled: Boolean,
    val success: Boolean
)
