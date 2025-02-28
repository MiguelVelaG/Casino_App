package com.miguel.casinoapp.data

import com.google.gson.annotations.SerializedName

data class DeckResponse(
    @SerializedName("deck_id") val deckId: String?,
    @SerializedName("remaining") val remaining: Int,
    @SerializedName("shuffled") val shuffled: Boolean,
    @SerializedName("success") val success: Boolean
)
