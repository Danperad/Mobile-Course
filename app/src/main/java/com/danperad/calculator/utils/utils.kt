package com.danperad.calculator.utils

import java.math.BigDecimal

fun convertToNumber(strNumber: String): BigDecimal? {
    if (strNumber.isNotEmpty() && Regex("^([+-]?)(0|([1-9]\\d*))(\\.\\d+)?\$").matches(strNumber)) {
        return BigDecimal(strNumber)
    }
    return null
}

fun convertToString(number: BigDecimal?): String {
    if (number == null)
        return ""
    if (number.abs() == BigDecimal(0.0))
        return "0"
    val parts = number.toString().split('.')
    if (parts.count() == 2 && parts[1].all { it == '0' })
        return number.toInt().toString()
    return number.toString()
}