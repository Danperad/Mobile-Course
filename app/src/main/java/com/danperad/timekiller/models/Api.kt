package com.danperad.timekiller.models

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {
    @Headers("Accept: Application/json")
    @GET("activity?")
    fun fetchActivity(
        @Query("accessibility") accessibility: Double,
        @Query("type") type: String,
        @Query("participants") participants: Int,
        @Query("price") price: Double,
    ): Call<Activity>
}