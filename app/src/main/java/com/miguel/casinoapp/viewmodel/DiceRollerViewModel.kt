package com.miguel.casinoapp.viewmodel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class DiceRollerViewModel : ViewModel() {
    var coins = mutableIntStateOf(1000)
        private set

    var betAmount = mutableIntStateOf(0)
        private set

    var selectedSide = mutableIntStateOf(1)
        private set

    var betResult = mutableStateOf("")
        private set

    fun updateCoins(newCoins: Int) {
        coins.intValue = newCoins
    }

    fun updateBetAmount(newBetAmount: Int) {
        betAmount.intValue = newBetAmount
    }

    fun updateSelectedSide(newSelectedSide: Int) {
        selectedSide.intValue = newSelectedSide
    }

    fun updateBetResult(newBetResult: String) {
        betResult.value = newBetResult
    }
}
