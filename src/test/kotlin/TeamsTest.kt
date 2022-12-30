package com.example.sergeybibikov.kotlin.api_tests

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

@DisplayName("Teams endpoint tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class TeamsTest {
    @BeforeAll
    fun healthCheck() = waitTillServiceIsUp(30)

    @Test
    fun allTeams() {
        val resp = ApiClient.getTeams()
        assertThat(resp.body()?.size).isEqualTo(30)
    }
}