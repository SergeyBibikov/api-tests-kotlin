package com.example.sergeybibikov.kotlin.api_tests

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
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

    @Test
    fun `Expecting fail as the password has no uppercase letters`() {
        val username = getRandomString(7)
        val email = getRandomString(7, false) + "@gmail.com"

        val response = ApiClient.register(username, "234ff3453535", email)
        val errorMessage = getResponseErrorMessage(response.errorBody()?.string())

        val expectedMessage = "The password must contain uppercase and lowercase letters"
        assertAll(
            { assertEquals(400, response.code()) },
            { assertEquals(expectedMessage, errorMessage) },
        )
    }

}