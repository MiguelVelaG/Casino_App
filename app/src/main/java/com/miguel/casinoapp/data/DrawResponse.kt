package com.miguel.casinoapp.data

data class DrawResponse(
    val success: Boolean,
    val cards: List<Card>,
    val deckId: String,
    val remaining: Int
)
