package com.example.sergeybibikov.kotlin.api_tests

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Ready {
    @GET("/ready")
    fun ready(): Call<ReadyResponseBody>
}

interface Register {
    @POST("/register")
    fun register(@Body body: RegisterRequestBody): Call<RegisterResponseBody>
}