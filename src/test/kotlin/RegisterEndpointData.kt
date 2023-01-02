package com.example.sergeybibikov.kotlin.api_tests

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterRequestBody(
    @SerializedName("username") val username: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("email") val email: String?,
)

data class RegisterResponseBody(
    @SerializedName("message") val message: String,
    @SerializedName("userId") val userId: Int,
)

interface Register {
    @POST("/register")
    fun register(@Body body: RegisterRequestBody): Call<RegisterResponseBody>
}
