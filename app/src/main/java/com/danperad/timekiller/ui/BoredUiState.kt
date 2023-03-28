package com.danperad.timekiller.ui

import com.danperad.timekiller.models.ActivityFilter
import com.danperad.timekiller.models.ActivityFindingResult

data class BoredUiState(
    val activityFilter: ActivityFilter,
    val activity: ActivityFindingResult? = null
)
