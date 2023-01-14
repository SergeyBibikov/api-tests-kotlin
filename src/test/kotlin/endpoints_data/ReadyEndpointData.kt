package com.example.sergeybibikov.kotlin.api_tests.endpoints_data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET

interface Ready {
    @GET("/ready")
    fun ready(): Call<ReadyResponseBody>
}

data class ReadyResponseBody(@SerializedName("status") val status: String)