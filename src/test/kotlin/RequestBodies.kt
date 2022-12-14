package com.example.sergeybibikov.kotlin.api_tests

import com.google.gson.annotations.SerializedName

data class RegisterRequestBody(
    @SerializedName("username") val username: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("email") val email: String?,
)