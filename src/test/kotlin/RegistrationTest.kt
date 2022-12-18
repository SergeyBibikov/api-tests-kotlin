package com.example.sergeybibikov.kotlin.api_tests


import io.qameta.allure.Feature
import org.junit.jupiter.api.*
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import kotlin.test.assertEquals

@DisplayName("Registration endpoint tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationTest {
    @BeforeAll
    fun healthCheck() {
        waitTillServiceIsUp(30)
    }

    @Test
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

        assertEquals(201, response.code())
        assertEquals("user created", respBody?.message)
        assertEquals(createdUser.id, receivedUserId)
    }

    //TODO: split into separate tests to use tags
    @TestFactory
    @Execution(ExecutionMode.CONCURRENT)
    @Feature("New user registration")
    fun invalidPasswords(): List<DynamicTest> {
        val username = getRandomString(7)
        val email = getRandomString(7, false) + "@gmail.com"
        val dt: MutableList<DynamicTest> = mutableListOf()

        data class Tc(val testName: String, val password: String, val expectedMessage: String)

        val charsCheckMessage = "The password must contain uppercase, lowercase letters and at least one number"
        listOf(
            Tc(
                "Should get 400 if less then 8 chars in password",
                "a4dd",
                "The password must be at least 8 characters long"
            ),
            Tc(
                "Should get 400 if no uppercase letters in password",
                "4asdfasdffdsdfsf",
                charsCheckMessage
            ),
            Tc(
                "Should get 400 if no numbers in password",
                "Asdfasdffdsdfsf",
                charsCheckMessage
            ),

            ).map {
            dt.add(DynamicTest.dynamicTest(it.testName) {
                val response = ApiClient.register(username, it.password, email)
                val errorMessage = getResponseErrorMessage(response.errorBody()?.string())

                assertAll(
                    { assertEquals(400, response.code()) },
                    { assertEquals(it.expectedMessage, errorMessage) },
                )
            })
        }

        return dt
    }

}