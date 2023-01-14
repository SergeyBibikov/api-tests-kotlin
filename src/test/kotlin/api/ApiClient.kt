package com.example.sergeybibikov.kotlin.api_tests.api

import com.example.sergeybibikov.kotlin.api_tests.endpoints_data.*
import com.google.gson.annotations.SerializedName
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
        fun register(username: String?, password: String?, email: String?)
                : Response<RegisterResponseBody> {
            return getBuiltClient(Register::class.java)
                .register(RegisterRequestBody(username, password, email))
                .execute()
        }

        @Step("Send GET request to /teams")
        fun getTeams(
            name: String? = null,
            conference: String? = null,
            division: String? = null,
            estYear: Int? = null
        ): Response<Array<Team>> {
            return getBuiltClient(Teams::class.java)
                .get(name, conference, division, estYear)
                .execute()
        }

        @Step("Send POST request to /token/get")
        fun getToken(username: String?, password: String?): Response<Token> {
            return getBuiltClient(GetToken::class.java)
                .get(GetTokenRequestBody(username, password))
                .execute()
        }

        @Step("Send POST request to /token/validate")
        fun validateToken(token: String?): Response<Unit> {
            return getBuiltClient(ValidateToken::class.java)
                .validate(ValidateTokenRequestBody(token))
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

data class ApiError(@SerializedName("error") val err: String)