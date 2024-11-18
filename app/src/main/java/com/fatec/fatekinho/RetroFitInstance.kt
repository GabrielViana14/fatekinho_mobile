package com.fatec.fatekinho

import com.fatec.fatekinho.interfaces.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://fatekinho-api-python-1.onrender.com")  // endpoint base
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}