package com.danperad.tictactoe.ui

import androidx.lifecycle.ViewModel
import com.danperad.tictactoe.models.Game
import com.danperad.tictactoe.models.GameConfiguration
import com.danperad.tictactoe.models.GameStateType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel(game: Game) : ViewModel() {
    private val _game: Game = game
    private val _uiState = MutableStateFlow(GameUiState(game))

    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    init {
        _game.createNewGame()
        _uiState.update { GameUiState(_game) }
    }
    fun zeroStep(iRow: Int, iCol: Int){
        _game.zeroStep(iRow, iCol)
        _uiState.update { GameUiState(_game) }
    }

    fun crossStep(iRow: Int, iCol: Int){
        _game.crossStep(iRow, iCol)
        _uiState.update { GameUiState(_game) }
    }

    fun startNewGame(){
        _game.createNewGame()
        _uiState.update { GameUiState(_game) }
    }

    fun getGameConfiguration(): GameConfiguration {
        return _game.getGameConfiguration()
    }

    fun changeTheme(){
        _isDarkTheme.update { !it  }
    }

    fun isGameStarted() = _game.getGameState().stateType == GameStateType.CrossStep ||  _game.getGameState().stateType == GameStateType.ZeroStep

}