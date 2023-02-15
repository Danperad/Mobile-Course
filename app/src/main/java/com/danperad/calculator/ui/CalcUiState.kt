package com.danperad.calculator.ui

import com.danperad.calculator.models.Calculator
import com.danperad.calculator.utils.convertToString

data class CalcUiState(
    val displayedNumber: String,
    val isInvalidNumber: Boolean
) {
    constructor(calculator: Calculator) :
        this(
            displayedNumber = convertToString(calculator.getLastNumber()),
            isInvalidNumber = false
        )
}