package com.danperad.timekiller.models

data class Activity(
    val activity: String,
    val accessibility: Double,
    val type: String,
    val participants: Int,
    val price: Double,
    val error: String? = null
)