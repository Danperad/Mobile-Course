package com.danperad.timekiller.models

val TYPES = listOf("education", "recreational", "social", "diy", "charity", "cooking", "relaxation", "music", "busywork")
val PARTICIPANTS = listOf(1,2,3,4,5,6,7,8,9,10)
data class ActivityFilter(
    val accessibility: Double = 0.0,
    val type: String = TYPES[0],
    val participants: Int = PARTICIPANTS[0],
    val price: Double = 0.0
)
