package com.danperad.timekiller.models

data class ActivityFindingResult(
    val finedActivity: Activity?,
    val isSuccess: Boolean = true,
    val errMsg: String? = null
)
