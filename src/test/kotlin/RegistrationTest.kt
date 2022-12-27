package com.example.sergeybibikov.kotlin.api_tests


import io.qameta.allure.Feature
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("Registration endpoint tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class RegistrationTest {
    @BeforeAll
    fun healthCheck() {
        waitTillServiceIsUp(30)
    }

    @Test
    @DisplayName("Successful registration")
    @Feature("New user registration")
    @Tag("Positive")
    fun `Successful registration`() {
        val username = getRandomString(7)
        val password = "A" + getRandomString(8, false) + "5"
        val email = getRandomString(7, false) + "@gmail.com"

        val response = ApiClient.register(username, password, email)
        val respBody = response.body()
        val receivedUserId = respBody?.userId
        val createdUser = DBClient().getUserByUsername(username)
        assertThat(response.code()).isEqualTo(201)
        assertThat(respBody?.message).isEqualTo("user created")
        assertThat(receivedUserId).isEqualTo(createdUser.id)
    }

    private val validUserName = getRandomString(7)
    private val validEmail = getRandomString(7, false) + "@gmail.com"

    @Feature("New user registration")
    @Tag("Negative")
    @DisplayName("Should get 400 if password ")
    @ParameterizedTest(name = "{1}")
    @CsvSource(
        "a4dd, is shorter then 8 chars, The password must be at least 8 characters long",
        "4asdfasdffdsdfsf, has no uppercase letters, default",
        "Asdfasdffdsdfsf, has no numbers, default"
    )
    fun invalidPassword(password: String, _ignore: String, message: String) {
        var expectedMessage = "The password must contain uppercase, lowercase letters and at least one number"
        if (message != "default") expectedMessage = message

        val response = ApiClient.register(validUserName, password, validEmail)
        val errorMessage = getResponseErrorMessage(response.errorBody()?.string())

        assertAll(
            { assertThat(response.code()).isEqualTo(400) },
            { assertThat(errorMessage).isEqualTo(expectedMessage) },
        )
    }

    @Feature("New user registration")
    @Tag("Negative")
    @DisplayName("Should get 400 if ")
    @ParameterizedTest(name = "no {0} in req body")
    @ValueSource(strings = ["email", "password", "username"])
    fun missingReqBodyField(missingField: String) {
        val expectedMessage = "Username, password and email are required"
        val validPassword = "Afdfd434fF"

        val resp = when (missingField) {
            "email" -> ApiClient.register(validUserName, validPassword, null)
            "username" -> ApiClient.register(null, validPassword, validEmail)
            else -> ApiClient.register(validUserName, null, validEmail)
        }
        val err = getResponseErrorMessage(resp.errorBody()?.string())

        assertThat(err).isEqualTo(expectedMessage)
    }
}
