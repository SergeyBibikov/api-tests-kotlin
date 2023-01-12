package com.example.sergeybibikov.kotlin.api_tests

import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("Token endpoints(get/validate) tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class TokenTest {
    @BeforeAll
    fun healthCheck() = waitTillServiceIsUp(30)

    @Feature("Getting a user token")
    @Tag("Positive")
    @Story("Successfully getting the token")
    @DisplayName("Successful token for user with role ")
    @ParameterizedTest(name = "<{0}>")
    @MethodSource("$TEST_DATA_CLASSNAME#roleAndUsername")
    fun positiveGetTokenTests(expectedRole: String, user: User) {

        val resp = ApiClient.getToken(user.username, user.password)
        val (roleParsed, _, uName) = resp.body()?.token!!.split("_")
        assertAll(
            { checkResponseStatus(resp, 200) },
            { checkValueEquality("the role in token", roleParsed, expectedRole) },
            { checkValueEquality("the username in token", uName, user.username) }
        )
    }

    @Feature("Getting a user token")
    @Tag("Negative")
    @Story("Error while getting a token")
    @DisplayName("No token in response if ")
    @ParameterizedTest(name = "{0}")
    @MethodSource("$TEST_DATA_CLASSNAME#getTokenInvalidData")
    @Suppress("UNUSED_PARAMETER")
    fun negativeGetTokenTests(_testName: String, reqB: GetTokenRequestBody, expectedErrorMsg: String) {

        val resp = ApiClient.getToken(reqB.username, reqB.password)

        assertAll(
            { checkResponseStatus(resp, 400) },
            { checkErrorMessage(resp, expectedErrorMsg) }
        )
    }

    @Feature("Validating the user token")
    @Tag("Positive")
    @Story("Successfully passed validation")
    @DisplayName("Successful validation of token ")
    @ParameterizedTest(name = "<{0}>")
    @ValueSource(strings = ["Admin_token_Jack", "Regular_token_Steve", "Premium_token_Mike"])
    fun positiveValidateTokenTests(token: String) {

        val resp = ApiClient.validateToken(token)

        assertAll(
            { checkResponseStatus(resp, 200) },
            { checkIsNull("response error msg", resp.errorBody()) },
            { checkValueEquality("response body", resp.body(), Unit) },
        )
    }

    @Feature("Validating the user token")
    @Tag("Negative")
    @Story("Token validation is not passed")
    @DisplayName("Validation error as ")
    @ParameterizedTest(name = "{0}")
    @MethodSource("$TEST_DATA_CLASSNAME#validateTokenInvalidData")
    @Suppress("UNUSED_PARAMETER")
    fun negativeValidateTokenTests(_testName: String, token: String?, expectedErrorMsg: String) {

        val resp = ApiClient.validateToken(token)

        assertAll(
            { checkResponseStatus(resp, 401) },
            { checkErrorMessage(resp, expectedErrorMsg)},
        )
    }
}