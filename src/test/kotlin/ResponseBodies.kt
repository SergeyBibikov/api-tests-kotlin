package com.example.sergeybibikov.kotlin.api_tests

import com.google.gson.annotations.SerializedName

data class ReadyResponseBody(@SerializedName("status") val status: String)

data class RegisterResponseBody(
    @SerializedName("message") val message: String,
    @SerializedName("userId") val userId: Int,
    )

data class ApiError(@SerializedName("error") val err: String)