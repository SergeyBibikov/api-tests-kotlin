package com.example.sergeybibikov.kotlin.api_tests

import io.qameta.allure.Step
import org.assertj.core.api.Assertions.assertThat
import retrofit2.Response

@Step("Check the response status code")
fun <T> checkResponseStatus(resp: Response<T>, expectedStatus: Int) {
    assertThat(resp.code()).isEqualTo(expectedStatus)
}
