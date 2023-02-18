package com.danperad.tictactoe.utils

import java.lang.Integer.min

inline fun <reified T> getFirstSimilarIRow(source: Array<Array<T>>, value: T): Int? {

    val rowLen: Int = source.size

    for (iRow in 0 until rowLen) {
        if (source[iRow].all { it == value }) {
            return iRow
        }
    }

    return null

}
inline fun <reified T> getFirstSimilarICol(source: Array<Array<T>>, value: T): Int? {

    val rowLen: Int = source.size
    val colLen: Int = source[0].size
    var isSimilarCol: Boolean

    for (iCol in 0 until colLen) {

        isSimilarCol = true

        for (iRow in 0 until rowLen) {

            if (source[iRow][iCol] != value) {
                isSimilarCol = false
                break
            }

        }

        if(isSimilarCol)
            return iCol

    }

    return null

}
inline fun <reified T> isSimilarMainDiag(source: Array<Array<T>>, value: T): Boolean {

    val rowLen: Int = source.size
    val colLen: Int = source[0].size

    for (i in 0 until min(rowLen, colLen)) {

        if (source[i][i] != value)
            return false

    }

    return true

}
inline fun <reified T> isSimilarSideDiag(source: Array<Array<T>>, value: T): Boolean {

    val rowLen: Int = source.size
    val colLen: Int = source[0].size

    for (i in 0 until min(rowLen, colLen)) {

        if (source[rowLen-i-1][i] != value)
            return false

    }

    return true

}
