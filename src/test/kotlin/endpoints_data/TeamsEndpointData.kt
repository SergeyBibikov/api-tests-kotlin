package com.example.sergeybibikov.kotlin.api_tests.endpoints_data

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

data class Team(
    val id: Int,
    val name: String,
    val conference: String,
    val division: String,
    @SerializedName("est_year") val estYear: Int
)

interface Teams {
    @GET("/teams")
    fun get(
        @Query("name") name: String?,
        @Query("conference") conference: String?,
        @Query("division") division: String?,
        @Query("est_year") year: Int?
    ): Call<Array<Team>>

    @DELETE("/teams/{id}")
    fun delete(@Header("Authorization") adminToken: String?, @Path("id") teamId: Int?):Call<Unit>

}