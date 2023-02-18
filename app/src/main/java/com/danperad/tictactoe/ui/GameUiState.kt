package com.danperad.tictactoe.ui

import com.danperad.tictactoe.models.Game
import com.danperad.tictactoe.models.GameStateType

data class GameUiState(
    val cells: Array<Array<CellButton>>,
    val stateType: GameStateType
) {
    constructor(game: Game) :
            this(
                cells = CellsConvertor(game).convert(),
                stateType = game.getGameState().stateType
            )

}