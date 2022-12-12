package com.example.sergeybibikov.kotlin.tests

import kotlin.test.Test
import kotlin.test.assertEquals
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

class ReadinessTest {

    @Test
    fun serviceIsReadyTest() {
        val request =
                Retrofit.Builder()
                        .baseUrl("http://localhost:8080")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(Ready::class.java)
        val respBody = request.heyReady().execute().body()
        assertEquals("ready", respBody?.status)
    }
}

interface Ready {
    @GET("/ready") fun heyReady(): Call<ReadyAnswer>
}
