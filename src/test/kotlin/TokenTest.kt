package com.example.sergeybibikov.kotlin.api_tests

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

@DisplayName("Token endpoints(get/validate) tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class TokenTest {
    @BeforeAll
    fun healthCheck() = waitTillServiceIsUp(30)

    @Test
    fun successfulGetTokenRequest() {
        val dbClient = DBClient()
        val user = dbClient.getRandomUser()
        val expectedRole = dbClient.getRoleById(user.roleId)

        val resp = ApiClient.getToken(user.username, user.password)
        val (role, _, uName) = resp.body()?.token!!.split("_")
        assertAll(
            { checkResponseStatus(resp, 200) },
            { assertThat(role).isEqualTo(expectedRole.name) },
            { assertThat(uName).isEqualTo(user.username) }
        )
    }
}