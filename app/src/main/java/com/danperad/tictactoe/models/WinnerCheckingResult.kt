package com.danperad.tictactoe.models

data class WinnerCheckingResult (
    val indexType: WinnerDirectionType,
    val index: Int? = null
)