package com.danperad.timekiller.models

import retrofit2.awaitResponse
import java.math.BigDecimal
import java.math.RoundingMode

class ActivityRepository {
    suspend fun findActivityByFilter(filter: ActivityFilter): ActivityFindingResult {
        try {
            val call = RestClient().getService()
                .fetchActivity(
                    BigDecimal(filter.accessibility).setScale(1, RoundingMode.HALF_UP).toDouble(),
                    filter.type,
                    filter.participants,
                    BigDecimal(filter.price).setScale(1, RoundingMode.HALF_UP).toDouble()
                )
            val response = call.awaitResponse()
            return if (response.isSuccessful && response.body()?.error == null) {
                ActivityFindingResult(response.body())
            } else if (response.isSuccessful && response.body()?.error != null) {
                return ActivityFindingResult(null, false, response.body()?.error)
            } else {
                return ActivityFindingResult(null, false, response.message())
            }
        } catch (e: Exception) {
            return ActivityFindingResult(null, false, e.message)
        }
    }
}