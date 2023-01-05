package com.example.sergeybibikov.kotlin.api_tests

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

@DisplayName("Token endpoints(get/validate) tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class TokenTest {
    @BeforeAll
    fun healthCheck() = waitTillServiceIsUp(30)
}