package com.example.sergeybibikov.kotlin.api_tests

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Ready {
    @GET("/ready")
    fun ready(): Call<ReadyResponseBody>
}

interface Register {
    @POST("/register")
    fun register(@Body body: RegisterRequestBody): Call<RegisterResponseBody>
}

interface Teams {
    @GET("/teams")
    fun get(
        @Query("name") name: String?,
        @Query("conference") conference: String?,
        @Query("division") division: String?,
        @Query("est_year") year: Int?
    ): Call<Array<Team>>
}