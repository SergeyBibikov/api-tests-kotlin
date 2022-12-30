package com.example.sergeybibikov.kotlin.api_tests

import com.google.gson.annotations.SerializedName

data class ReadyResponseBody(@SerializedName("status") val status: String)

data class RegisterResponseBody(
    @SerializedName("message") val message: String,
    @SerializedName("userId") val userId: Int,
)

data class ApiError(@SerializedName("error") val err: String)

data class Team(
    val id: Int,
    val name: String,
    val conference: String,
    val division: String,
    @SerializedName("est_year") val estYear: Int
)

