package com.example.sergeybibikov.kotlin.api_tests

import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

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
    fun successfulGetTokenRequest(expectedRole: String, user: User) {

        val resp = ApiClient.getToken(user.username, user.password)
        val (roleParsed, _, uName) = resp.body()?.token!!.split("_")

        assertAll(
            { checkResponseStatus(resp, 200) },
            { assertThat(roleParsed).isEqualTo(expectedRole) },
            { assertThat(uName).isEqualTo(user.username) }
        )
    }
}