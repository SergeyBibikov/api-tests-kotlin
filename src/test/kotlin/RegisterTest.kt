package com.example.sergeybibikov.kotlin.api_tests


import com.example.sergeybibikov.kotlin.api_tests.api.ApiClient
import com.example.sergeybibikov.kotlin.api_tests.db.DBClient
import com.example.sergeybibikov.kotlin.api_tests.test_data.*
import com.example.sergeybibikov.kotlin.api_tests.utils.*
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@DisplayName("Registration endpoint tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class RegisterTest {
    @BeforeAll
    fun healthCheck() {
        waitTillServiceIsUp(30)
    }

    @Feature("New user registration")
    @Story("Successful registration")
    @Tag("Positive")
    @DisplayName("Registration with email = ")
    @ParameterizedTest(name = "{0}")
    @MethodSource("$TEST_DATA_CLASSNAME#successfulRegData")
    fun `Successful registration`(email: String) {
        val username = getRandomString(7)
        val password = "A" + getRandomString(8, false) + "5"

        val response = ApiClient.register(username, password, email)
        checkResponseStatus(response, 201)

        val respBody = response.body()
        val receivedUserId = respBody?.userId
        val query = "select * from users where username = '$username'"
        val createdUser = DBClient().getSingleUser(query)

        assertAll(
            { checkValueEquality("the response message", respBody?.message, "user created") },
            { checkValueEquality("the user id in response", receivedUserId, createdUser.id) },
        )
    }

    @Feature("New user registration")
    @Tag("Negative")
    @Story("Missing registration request body fields")
    @DisplayName("Should get 400 if ")
    @ParameterizedTest(name = "{3}")
    @MethodSource("$TEST_DATA_CLASSNAME#missingFieldsData")
    @Suppress("UNUSED_PARAMETER")
    fun missingReqBodyField(username: String?, password: String?, email: String?, testName: String) {
        val expectedMessage = "Username, password and email are required"
        val resp = ApiClient.register(username, password, email)
        checkErrorMessage(resp, expectedMessage)
    }

    @Feature("New user registration")
    @Story("Invalid password in the reg. request body")
    @Tag("Negative")
    @DisplayName("Should get 400 if password ")
    @ParameterizedTest(name = "{1}")
    @MethodSource("$TEST_DATA_CLASSNAME#invalidPasswordData")
    @Suppress("UNUSED_PARAMETER")
    fun invalidPassword(password: String, _ignore: String, message: String) {
        var expectedMessage = "The password must contain uppercase, lowercase letters and at least one number"
        if (message != "default") expectedMessage = message

        val response = ApiClient.register(getValidUsername(), password, getValidEmail())

        assertAll(
            { checkResponseStatus(response, 400) },
            { checkErrorMessage(response, expectedMessage) },
        )
    }

    @Feature("New user registration")
    @Story("Invalid email in the reg. request body")
    @Tag("Negative")
    @DisplayName("Should get 400 if email ")
    @ParameterizedTest(name = "{1}")
    @MethodSource("$TEST_DATA_CLASSNAME#invalidEmailsData")
    @Suppress("UNUSED_PARAMETER")
    fun invalidEmail(email: String, _ignore: String) {
        val expectedMessage = "The email has an invalid format"

        val response = ApiClient.register(getValidUsername(), getValidPassword(), email)

        assertAll(
            { checkResponseStatus(response, 400) },
            { checkErrorMessage(response, expectedMessage) },
        )
    }
}
