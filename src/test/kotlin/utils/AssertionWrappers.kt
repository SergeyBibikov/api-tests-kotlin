package com.example.sergeybibikov.kotlin.api_tests.utils

import io.qameta.allure.Step
import org.assertj.core.api.Assertions.assertThat
import retrofit2.Response


@Step("Check that the response status code is {1}")
fun <T> checkResponseStatus(resp: Response<T>, expectedStatus: Int) {
    assertThat(resp.code()).isEqualTo(expectedStatus)
}

@Step("Check that {0} is {2}")
@Suppress("UNUSED_PARAMETER")
fun <T> checkValueEquality(checkedValueDefinition: String, actualValue: T, expectedValue: T) {
    assertThat(actualValue).isEqualTo(expectedValue)
}

@Step("Check the response error message")
fun <T> checkErrorMessage(resp: Response<T>, expectedErrorMsg: String?) {
    val actualError = getResponseErrorMessage(resp.errorBody()?.string())
    assertThat(actualError).isEqualTo(expectedErrorMsg)
}

@Step("Check that {0} is null")
@Suppress("UNUSED_PARAMETER")
fun <T> checkIsNull(checkedValueDefinition: String, actualValue: T) {
    assertThat(actualValue).isNull()
}