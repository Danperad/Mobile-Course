package com.danperad.tictactoe.ui

import com.danperad.tictactoe.models.CellState
import com.danperad.tictactoe.models.Game
import com.danperad.tictactoe.models.*

class CellsConvertor(private val game: Game) {

    @Suppress("UNCHECKED_CAST")
    fun convert(): Array<Array<CellButton>> {

        val rowlen = game.getGameConfiguration().boxSize
        val collen = game.getGameConfiguration().boxSize
        val cells = game.getGameState().world

        val cellButtons: ArrayList<Array<CellButton>> = ArrayList()
        var row: ArrayList<CellButton>

        for (iRow in 0 until rowlen) {

            row = ArrayList()

            for (iCol in 0 until collen) {
                row.add(
                    CellButton(
                        cells[iRow][iCol],
                        cells[iRow][iCol] == CellState.Empty,
                        isWinLine(iRow, iCol), iRow, iCol
                    )
                )
            }

            cellButtons.add(row.toTypedArray())
        }
        return cellButtons.toTypedArray()

    }

    private fun isWinLine(iRow: Int, iCol: Int): Boolean {

        val len = game.getGameConfiguration().boxSize
        val winState = game.getGameState().winnerCheckingResult

        if (winState != null) {
            if (winState.indexType == WinnerDirectionType.Row && iRow == winState.index)
                return true
            else if (winState.indexType == WinnerDirectionType.Column && iCol == winState.index)
                return true
            else if (winState.indexType == WinnerDirectionType.MainDiag && iRow == iCol)
                return true
            else if (winState.indexType == WinnerDirectionType.SideDiag && iRow + iCol == len - 1)
                return true

        }

        return false

    }
}
