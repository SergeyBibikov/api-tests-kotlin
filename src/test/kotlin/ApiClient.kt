package com.example.sergeybibikov.kotlin.api_tests

import io.qameta.allure.Step
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {
        fun ready(): Response<ReadyResponseBody> {
            return getBuiltClient(Ready::class.java).ready().execute()
        }

        @Step("Send POST request to /registration")
        fun register(username: String? = null, password: String?, email: String?)
                : Response<RegisterResponseBody> {
            return getBuiltClient(Register::class.java)
                .register(RegisterRequestBody(username, password, email))
                .execute()
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
