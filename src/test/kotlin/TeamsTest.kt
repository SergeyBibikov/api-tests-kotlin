package com.example.sergeybibikov.kotlin.api_tests

import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource

@Feature("Getting teams list")
@Test
@Tag("Negative")
annotation class TeamsNegativeTest

@Feature("Getting teams list")
@Tag("Positive")
annotation class TeamsPositiveTest

@DisplayName("Teams endpoint tests")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.CONCURRENT)
class TeamsTest {
    @BeforeAll
    fun healthCheck() = waitTillServiceIsUp(30)

    @Test
    @TeamsPositiveTest
    @DisplayName("Should return all 30 teams if no params are provided")
    fun allTeams() {
        val resp = ApiClient.getTeams()
        assertAll(
            { checkResponseStatus(resp, 200) },
            { assertThat(resp.body()?.size).isEqualTo(30) })
    }

    @TeamsPositiveTest
    @Test
    @DisplayName("Should get one team with name Utah Jazz")
    fun getTeamByName() {
        val teamName = "Utah Jazz"
        val resp = ApiClient.getTeams(name = teamName)
        val body = resp.body()
        assertAll(
            { checkResponseStatus(resp, 200) },
            { assertThat(body?.size).isEqualTo(1) },
            { assertThat(body?.get(0)?.name).isEqualTo(teamName) })
    }

    @DisplayName("Should no teams with name Utam Jazz")
    @TeamsNegativeTest
    fun noTeamsWithInvalidName() {
        val teamName = "Utam Jazz"
        val resp = ApiClient.getTeams(name = teamName)
        val body = resp.body()
        assertAll(
            { checkResponseStatus(resp, 200) },
            { assertThat(body?.size).isEqualTo(0) },
        )
    }

    @TeamsPositiveTest
    @Story("Filtering teams by conference")
    @DisplayName("Should get only teams from conf:")
    @ParameterizedTest(name = "{0}")
    @MethodSource("$TEST_DATA_CLASSNAME#conferencesData")
    fun getTeamsFromConference(conf: String) {
        val resp = ApiClient.getTeams(conference = conf)
        val body = resp.body()
        val normalizedConf = conf.lowercase().replaceFirstChar { it.uppercase() }
        assertAll(
            { checkResponseStatus(resp, 200) },
            { assertThat(body?.size).isEqualTo(15) },
            { assertThat(body?.filter { it.conference != normalizedConf }).isEmpty() })
    }

    @TeamsNegativeTest
    @Story("Filtering teams by conference")
    @DisplayName("Should get 400 if the conference is not East or West")
    fun invalidConference() {
        val resp = ApiClient.getTeams(conference = "South")
        val err = getResponseErrorMessage(resp.errorBody()?.string())
        val expectedErrorMsg = "allowed conferences are: East, West"
        assertAll(
            { checkResponseStatus(resp, 400) },
            { assertThat(err).isEqualTo(expectedErrorMsg) }
        )
    }

    @TeamsPositiveTest
    @Story("Filtering teams by division")
    @DisplayName("Should get only teams from div:")
    @ParameterizedTest(name = "{0}")
    @MethodSource("$TEST_DATA_CLASSNAME#divisionsData")
    fun getTeamsFromDivision(div: String) {
        val resp = ApiClient.getTeams(division = div)
        val body = resp.body()
        val normalizedConf = div.lowercase().replaceFirstChar { it.uppercase() }
        assertAll(
            { checkResponseStatus(resp, 200) },
            { assertThat(body?.size).isEqualTo(5) },
            { assertThat(body?.filter { it.division != normalizedConf }).isEmpty() })
    }

    @Story("Filtering teams by division")
    @TeamsNegativeTest
    @DisplayName("Should get 400 if the division is not from the supported list")
    fun invalidDivision() {
        val resp = ApiClient.getTeams(division = "Northeast")
        val err = getResponseErrorMessage(resp.errorBody()?.string())
        val expectedErrorMsg = "allowed conferences are: Atlantic, Pacific, Southeast, Central, Northwest, Southwest"
        assertAll(
            { checkResponseStatus(resp, 400) },
            { assertThat(err).isEqualTo(expectedErrorMsg) }
        )
    }

    @TeamsPositiveTest
    @Story("Getting teams est. in a specific year")
    @DisplayName("Should get only one team est. in ")
    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = [2008, 1984, 1997, 2001, 1960])
    fun yearsWhenOneTeamWasEstablished(year: Int) {
        val resp = ApiClient.getTeams(estYear = year)
        val body = resp.body()
        assertAll(
            { checkResponseStatus(resp, 200) },
            { assertThat(body?.size).isEqualTo(1) },
            { assertThat(body?.get(0)?.estYear).isEqualTo(year) }
        )

    }

    @TeamsPositiveTest
    @Story("Getting teams est. in a specific year")
    @DisplayName("Should get two teams est. in ")
    @ParameterizedTest(name = "{0}")
    @ValueSource(ints = [1989, 1971, 1970, 1946, 1976])
    fun yearsWhenTwoTeamsWereEstablished(year: Int) {
        val resp = ApiClient.getTeams(estYear = year)
        val body = resp.body()
        assertAll(
            { checkResponseStatus(resp, 200) },
            { assertThat(body?.size).isEqualTo(2) },
            { assertThat(body?.filter { it.estYear != year }).isEmpty() }
        )

    }

    @Test
    @TeamsPositiveTest
    @Story("Getting teams est. in a specific year")
    @DisplayName("Should get three teams est. in 1968")
    fun yearWhenThreeTeamsWereEstablished() {
        val yearWithThreeTeams = 1968
        val resp = ApiClient.getTeams(estYear = yearWithThreeTeams)
        val body = resp.body()
        assertAll(
            { checkResponseStatus(resp, 200) },
            { assertThat(body?.size).isEqualTo(3) },
            { assertThat(body?.filter { it.estYear != yearWithThreeTeams }).isEmpty() }
        )
    }

    @TeamsNegativeTest
    @Story("Getting teams est. in a specific year")
    @DisplayName("Should get no teams est. in 2020")
    fun yearWhenNOTeamsWereEstablished() {
        val yearWithNoTeams = 2020
        val resp = ApiClient.getTeams(estYear = yearWithNoTeams)
        val body = resp.body()
        assertAll(
            { checkResponseStatus(resp, 200) },
            { assertThat(body?.size).isEqualTo(0) },
        )
    }
}
