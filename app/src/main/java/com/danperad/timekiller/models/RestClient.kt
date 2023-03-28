package com.danperad.timekiller.models

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClient {
    private val service: Api
    fun getService(): Api = service

    init {
        val baseUrl = "https://www.boredapi.com/api/"
        val client = OkHttpClient.Builder()
        client.connectTimeout(2, TimeUnit.MINUTES)
        client.readTimeout(2, TimeUnit.MINUTES)
        client.writeTimeout(2, TimeUnit.MINUTES)
        val gson = GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson))
            .client(client.build()).build()
        service = retrofit.create(Api::class.java)
    }
}