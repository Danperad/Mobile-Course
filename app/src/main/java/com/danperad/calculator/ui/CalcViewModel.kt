package com.danperad.calculator.ui

import androidx.lifecycle.ViewModel
import com.danperad.calculator.models.Calculator
import com.danperad.calculator.models.OperationType
import com.danperad.calculator.utils.convertToNumber
import com.danperad.calculator.utils.convertToString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CalcViewModel(calculator: Calculator) : ViewModel() {
    private val _calculator: Calculator = calculator
    private val _uiState = MutableStateFlow(CalcUiState(calculator))

    val uiState: StateFlow<CalcUiState> = _uiState.asStateFlow()

    fun appendCharToCurNumber(symbols: Char) {
        _uiState.update { it.copy(displayedNumber = it.displayedNumber + symbols, isInvalidNumber = false) }
    }

    fun appendStrToCurNumber(string: String) {
        _uiState.update { it.copy(displayedNumber = it.displayedNumber + string, isInvalidNumber = false) }

    }

    fun removeLastCharFromCurNumber() {
        _uiState.update { it.copy(displayedNumber = it.displayedNumber.dropLast((1)), isInvalidNumber = false) }
    }

    fun resetState(isInvalidNumber: Boolean = false) {
        _calculator.reset()
        _uiState.update { CalcUiState(_calculator).copy(isInvalidNumber = isInvalidNumber) }
    }

    fun runOperation(opType: OperationType) {
        if (pushNumber()) {
            _calculator.runOperation()
            _calculator.setOperation(opType)
            if (opType == OperationType.Empty) {
                _uiState.update {
                    CalcUiState(_calculator).copy(displayedNumber = convertToString(_calculator.getLastNumber()))
                }
            } else
                _uiState.update { CalcUiState(_calculator).copy(displayedNumber = "") }

        } else
            resetState(true)
    }

    private fun pushNumber(): Boolean {
        val curState = _uiState.asStateFlow().value
        val convertedNumber = convertToNumber(curState.displayedNumber) ?: return false
        _calculator.setNumber(convertedNumber)
        return true
    }
}