package com.example.sergeybibikov.kotlin.api_tests

import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

@DisplayName("Teams endpoint tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class TeamsTest {
    @BeforeAll
    fun healthCheck() = waitTillServiceIsUp(30)

    @Feature("Getting teams list")
    @Test
    @Tag("Positive")
    @DisplayName("Should return all 30 teams if no params are provided")
    fun allTeams() {
        val resp = ApiClient.getTeams()
        assertAll(
            { assertThat(resp.code()).isEqualTo(200) },
            { assertThat(resp.body()?.size).isEqualTo(30) })
    }

    @Feature("Getting teams list")
    @Story("Filtering teams by conference")
    @Tag("Positive")
    @DisplayName("Should get only teams from conf:")
    @ParameterizedTest(name = "{0}")
    @MethodSource("$TEST_DATA_CLASSNAME#conferencesData")
    fun getTeamsFromConference(conf: String) {
        val resp = ApiClient.getTeams(conference = conf)
        val body = resp.body()
        val normalizedConf = conf.lowercase().replaceFirstChar { it.uppercase() }
        assertAll(
            { assertThat(resp.code()).isEqualTo(200) },
            { assertThat(body?.size).isEqualTo(15) },
            { assertThat(body?.filter { it.conference != normalizedConf }).isEmpty() })
    }

    @Test
    @Feature("Getting teams list")
    @Story("Filtering teams by conference")
    @Tag("Negative")
    @DisplayName("Should get 400 if the conference is not East or West")
    fun invalidConference() {
        val resp = ApiClient.getTeams(conference = "South")
        val err = getResponseErrorMessage(resp.errorBody()?.string())
        val expectedErrorMsg = "allowed conferences are: East, West"
        assertAll(
            { assertThat(resp.code()).isEqualTo(400) },
            { assertThat(err).isEqualTo(expectedErrorMsg) }
        )
    }

    @Feature("Getting teams list")
    @Story("Filtering teams by division")
    @Tag("Positive")
    @DisplayName("Should get only teams from div:")
    @ParameterizedTest(name = "{0}")
    @MethodSource("$TEST_DATA_CLASSNAME#divisionsData")
    fun getTeamsFromDivision(div: String) {
        val resp = ApiClient.getTeams(division = div)
        val body = resp.body()
        val normalizedConf = div.lowercase().replaceFirstChar { it.uppercase() }
        assertAll(
            { assertThat(resp.code()).isEqualTo(200) },
            { assertThat(body?.size).isEqualTo(5) },
            { assertThat(body?.filter { it.division != normalizedConf }).isEmpty() })
    }

    @Feature("Getting teams list")
    @Story("Filtering teams by division")
    @Test
    @Tag("Negative")
    @DisplayName("Should get 400 if the division is not from the supported list")
    fun invalidDivision() {
        val resp = ApiClient.getTeams(division = "Northeast")
        val err = getResponseErrorMessage(resp.errorBody()?.string())
        val expectedErrorMsg = "allowed conferences are: Atlantic, Pacific, Southeast, Central, Northwest, Southwest"
        assertAll(
            { assertThat(resp.code()).isEqualTo(400) },
            { assertThat(err).isEqualTo(expectedErrorMsg) }
        )
    }
}