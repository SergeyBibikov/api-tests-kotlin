package com.example.sergeybibikov.kotlin.api_tests

import org.junit.jupiter.api.*
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationTest {
    @BeforeAll
    fun healthCheck() {
        waitTillServiceIsUp(30)
    }

    @Test
    fun `Successful registration`() {
        val username = getRandomString(7)
        val password = "A" + getRandomString(8, false)
        val email = getRandomString(7, false) + "@gmail.com"

        val response = ApiClient.register(username, password, email)
        val respBody = response.body()
        val receivedUserId = respBody?.userId
        val createdUser = DBClient().getUserByUsername(username)

        assertEquals(201, response.code())
        assertEquals("user created", respBody?.message)
        assertEquals(createdUser.id, receivedUserId)
    }

    @TestFactory
    fun invalidPasswords(): List<DynamicTest> {
        val username = getRandomString(7)
        val email = getRandomString(7, false) + "@gmail.com"
        val dt: MutableList<DynamicTest> = mutableListOf()

        data class Tc(val testName: String, val password: String, val expectedMessage: String)

        listOf(
            Tc(
                "Should get 400 if no uppercase letters in password",
                "asdfasdffdsdfsf",
                "The password must contain uppercase and lowercase letters"
            ),
            Tc(
                "Should get 400 if less then 8 chars in password",
                "add",
                "The password must be at least 8 characters long"
            )
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