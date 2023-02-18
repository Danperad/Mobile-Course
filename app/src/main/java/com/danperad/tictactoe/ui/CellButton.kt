package com.danperad.tictactoe.ui

import com.danperad.tictactoe.models.CellState

data class CellButton(val cellType: CellState, val enabled: Boolean, val isWinLine: Boolean, val rowNumber: Int, val columnNumber: Int) {
    constructor(rowNumber: Int, columnNumber: Int):
            this(cellType = CellState.Empty, enabled = true, isWinLine = false, rowNumber, columnNumber)
}