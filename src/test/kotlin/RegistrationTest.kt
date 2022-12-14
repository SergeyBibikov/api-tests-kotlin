package com.example.sergeybibikov.kotlin.api_tests

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationTest {
    @BeforeAll
    fun healthCheck(){
        waitTillServiceIsUp(30)
    }
    //TODO: add random values for inputs
    @Test
    fun `Successful registration`(){
        val response = ApiClient.register("Michael", "234Ff3453535", "test@gmail.com")
        assertEquals(201, response.code())
    }
    @Test
    fun `Expecting fail as the password has no uppercase letters`(){
        val expectedMessage = "The password must contain uppercase and lowercase letters"
        val response = ApiClient.register("Michael12", "234ff3453535", "1test@gmail.com")
        val errorMessage = getResponseErrorMessage(response.errorBody()?.string())
        assertAll(
            { assertEquals(400, response.code()) },
            { assertEquals(expectedMessage, errorMessage) },
        )
    }
}