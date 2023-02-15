package com.danperad.calculator.models

import java.math.BigDecimal

class Calculator(
    private var firstNumber: BigDecimal? = null,
    private var secondNumber: BigDecimal? = null,
    private var currentOperation: OperationType = OperationType.Empty
) {
    fun runOperation() {
        when (currentOperation) {
            OperationType.Empty -> doEmptyOperation()
            OperationType.Add -> doAddOperation()
            OperationType.Sub -> doSubOperation()
            OperationType.Mul -> doMulOperation()
            OperationType.Div -> doDivOperation()
            OperationType.DivPart -> doDivPartOperation()
        }
    }

    fun reset() {
        firstNumber = null
        secondNumber = null
        currentOperation = OperationType.Empty
    }

    fun getLastNumber(): BigDecimal? {
        return if (secondNumber != null)
            secondNumber
        else
            firstNumber
    }

    fun setNumber(number: BigDecimal?) {
        if (firstNumber == null)
            firstNumber = number
        else
            secondNumber = number
    }

    fun setOperation(opType: OperationType) {
        currentOperation = opType
    }

    private fun doEmptyOperation() {
        if (secondNumber != null) {
            firstNumber = secondNumber
            secondNumber = null
        }
    }

    private fun doAddOperation() {
        if (canDoCalcOperation()) {
            firstNumber = firstNumber!! + secondNumber!!
            secondNumber = null
        }
    }

    private fun doSubOperation() {
        if (canDoCalcOperation()) {
            firstNumber = firstNumber!! - secondNumber!!
            secondNumber = null
        }
    }

    private fun doMulOperation() {
        if (canDoCalcOperation()) {
            firstNumber = firstNumber!! * secondNumber!!
            secondNumber = null
        }
    }

    private fun doDivOperation() {
        if (canDoCalcOperation()) {
            firstNumber = firstNumber!!.divide(secondNumber!!, BigDecimal.ROUND_CEILING)
            secondNumber = null
        }
    }

    private fun doDivPartOperation() {
        if (canDoCalcOperation()) {
            firstNumber = firstNumber!!.rem( secondNumber!!)
            secondNumber = null
        }
    }

    private fun canDoCalcOperation(): Boolean = firstNumber != null && secondNumber != null
}