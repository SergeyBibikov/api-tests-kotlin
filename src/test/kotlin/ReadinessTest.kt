package com.example.sergeybibikov.kotlin.api_tests

import com.google.gson.annotations.SerializedName
import kotlin.test.Test
import kotlin.test.assertEquals
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

interface Ready {
    @GET("/ready") fun heyReady(): Call<ReadyAnswer>
}

data class ReadyAnswer(@SerializedName("status") val status: String)

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
