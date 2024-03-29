package com.example.sergeybibikov.kotlin.api_tests.endpoints_data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class GetTokenRequestBody(val username: String?, val password: String?)
data class ValidateTokenRequestBody(val token: String?)
data class Token(val token: String)

interface GetToken {
    @POST("/token/get")
    fun get(@Body body: GetTokenRequestBody): Call<Token>
}

interface ValidateToken {
    @POST("/token/validate")
    fun validate(@Body body: ValidateTokenRequestBody): Call<Unit>
}