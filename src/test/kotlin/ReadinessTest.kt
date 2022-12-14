package com.example.sergeybibikov.kotlin.api_tests


import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReadyTest {
    @BeforeAll
    fun healthCheck(){
        waitTillServiceIsUp(30)
    }
    @Test
    fun `The message in body should equal ready`() {
        val respBody = ApiClient.ready().body()

        assertEquals("ready", respBody?.status)

    }

    @Test
    fun `The status code should equal 200`() {
        val response = ApiClient.ready()

        assertEquals(200, response.code())
    }

}
