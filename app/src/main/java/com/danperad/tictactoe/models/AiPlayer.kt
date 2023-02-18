package com.danperad.tictactoe.models

import com.danperad.tictactoe.utils.*

internal class AiPlayer(private val game: Game){

    private var nextPos: Pair<Int, Int> = Pair(0,0)

    fun getNextStep():Pair<Int, Int>{

        val currentPlayer =
            if(game.getGameState().stateType == GameStateType.CrossStep)
                CellState.Cross
            else
                CellState.Zero

        minimax(game.getGameState().world.clone(), currentPlayer)

        return nextPos
    }

    private fun minimax(cells: Array<Array<CellState>>, currentPlayer: CellState): Int{

        if(gameCompleted(cells))
            return getScores(cells)

        val calcResult: Triple<Int, Int, Int>
        val scoresToPos: ArrayList<Triple<Int, Int, Int>> = arrayListOf()

        for(iRow in 0 until game.getGameConfiguration().boxSize){
            for(iCol in 0 until game.getGameConfiguration().boxSize){

                if(cells[iRow][iCol] == CellState.Empty){

                    val newCells = cells.map{ it.clone() }.toTypedArray()
                    newCells[iRow][iCol] = currentPlayer
                    scoresToPos.add(Triple(iRow, iCol, minimax(newCells, getNextPlayer(currentPlayer))))
                }

            }
        }

        calcResult = if (currentPlayer == getPlayerSymbol()){
            scoresToPos.minBy { it.third }
        }else{
            scoresToPos.maxBy { it.third }
        }

        nextPos = Pair(calcResult.first, calcResult.second)

        return calcResult.third

    }

    private fun getScores(cells: Array<Array<CellState>>): Int{

        return if(isWin(cells, getComputerSymbol()))
            10
        else if(isWin(cells, getPlayerSymbol()))
            -10
        else
            0

    }

    private fun gameCompleted(cells: Array<Array<CellState>>): Boolean{

        return allBusyCells(cells) ||
                isWin(cells, getPlayerSymbol()) ||
                isWin(cells, getComputerSymbol())

    }

    private fun allBusyCells(cells: Array<Array<CellState>>): Boolean{

        for(iRow in cells.indices){
            for(iCol in cells.indices){

                if(cells[iRow][iCol] == CellState.Empty)
                    return false

            }
        }

        return true

    }

    private fun isWin(cells: Array<Array<CellState>>, cellType: CellState): Boolean{

        return getFirstSimilarIRow(cells, cellType) != null ||
                getFirstSimilarICol(cells, cellType) != null ||
                isSimilarMainDiag(cells, cellType) ||
                isSimilarSideDiag(cells, cellType)

    }

    private fun getComputerSymbol(): CellState{

        return if(game.getGameConfiguration().mode == GameMode.CrossComputer)
            CellState.Cross
        else
            CellState.Zero

    }

    private fun getPlayerSymbol(): CellState{

        return if(game.getGameConfiguration().mode == GameMode.CrossComputer)
            CellState.Zero
        else
            CellState.Cross

    }

    private fun getNextPlayer(currentPlayer: CellState): CellState{

        return if(currentPlayer == CellState.Zero)
            CellState.Cross
        else
            CellState.Zero

    }
}
