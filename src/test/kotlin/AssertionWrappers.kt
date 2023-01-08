package com.example.sergeybibikov.kotlin.api_tests

import io.qameta.allure.Step
import org.assertj.core.api.Assertions.assertThat
import retrofit2.Response

@Step("Check the response status code")
fun <T> checkResponseStatus(resp: Response<T>, expectedStatus: Int) {
    assertThat(resp.code()).isEqualTo(expectedStatus)
}

@Step("Check that {0} is {2}")
fun <T> checkValueEquality(checkedValueName: String, actualValue: T, expectedValue: T) {
    assertThat(actualValue).isEqualTo(expectedValue)
}