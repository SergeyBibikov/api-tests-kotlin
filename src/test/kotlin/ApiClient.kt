package com.example.sergeybibikov.kotlin.api_tests

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface Ready {
    @GET("/ready")
    fun ready(): Call<ReadyAnswer>
}

data class ReadyAnswer(@SerializedName("status") val status: String)

class ApiClient {
    companion object {
        fun ready(): Response<ReadyAnswer> {
            return getBuiltClient(Ready::class.java).ready().execute()
        }
    }

}

fun <T> getBuiltClient(classToCreate: Class<T>): T {
    return Retrofit.Builder()
        .baseUrl("http://localhost:8080")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(classToCreate)
}
