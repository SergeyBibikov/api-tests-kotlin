package com.example.sergeybibikov.kotlin.api_tests

import com.example.sergeybibikov.kotlin.api_tests.api.ApiClient
import com.example.sergeybibikov.kotlin.api_tests.db.DBClient
import com.example.sergeybibikov.kotlin.api_tests.test_data.TEST_DATA_CLASSNAME
import com.example.sergeybibikov.kotlin.api_tests.utils.*
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
            { checkValueEquality("the teams array length", resp.body()?.size, 30) }
        )
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
            { checkValueEquality("the teams array length", body?.size, 1) },
            { checkValueEquality("the returned team's name", body?.get(0)?.name, teamName) },
        )
    }

    @DisplayName("Should no teams with name Utam Jazz")
    @TeamsNegativeTest
    fun noTeamsWithInvalidName() {
        val teamName = "Utam Jazz"
        val resp = ApiClient.getTeams(name = teamName)
        val body = resp.body()
        assertAll(
            { checkResponseStatus(resp, 200) },
            { checkValueEquality("the teams array length", body?.size, 0) },
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
            { checkValueEquality("the teams array length", body?.size, 15) },
            { assertThat(body?.filter { it.conference != normalizedConf }).isEmpty() })
    }

    @TeamsNegativeTest
    @Story("Filtering teams by conference")
    @DisplayName("Should get 400 if the conference is not East or West")
    fun invalidConference() {
        val resp = ApiClient.getTeams(conference = "South")
        val expectedErrorMsg = "allowed conferences are: East, West"

        assertAll(
            { checkResponseStatus(resp, 400) },
            { checkErrorMessage(resp, expectedErrorMsg) }
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
            { checkValueEquality("the teams array length", body?.size, 5) },
            { assertThat(body?.filter { it.division != normalizedConf }).isEmpty() })
    }

    @Story("Filtering teams by division")
    @TeamsNegativeTest
    @DisplayName("Should get 400 if the division is not from the supported list")
    fun invalidDivision() {
        val resp = ApiClient.getTeams(division = "Northeast")
        val expectedErrorMsg = "allowed conferences are: Atlantic, Pacific, Southeast, Central, Northwest, Southwest"

        assertAll(
            { checkResponseStatus(resp, 400) },
            { checkErrorMessage(resp, expectedErrorMsg) }
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
            { checkValueEquality("the teams array length", body?.size, 1) },
            { checkValueEquality("the established year", body?.get(0)?.estYear, year) },
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
            { checkValueEquality("the teams array length", body?.size, 2) },
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
            { checkValueEquality("the teams array length", body?.size, 3) },
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
            { checkValueEquality("the teams array length", body?.size, 0) },
        )
    }

    @Test
    @Feature("Team deletion")
    @Story("Successful team deletion")
    @DisplayName("Successful team deletion by an admin")
    fun successfullyDeleteTeam() {
        val teamName = "testName"
        val i = DBClient().insert(
            """
            insert into 
                teams(name, conference, division, est_year) 
                values('$teamName', 'East', 'Atlantic', 2022)
            returning id
            """.trimIndent()
        )

        val token = getUserTokenWithRole("Admin")
        ApiClient.deleteTeam(token, i)

        val after = ApiClient.getTeams(name = teamName).body()?.size

        assertAll(
            { checkValueEquality("the teams response array length", after, 0) }
        )
    }

    @Feature("Team deletion")
    @Story("Unsuccessful team deletion attempt")
    @DisplayName("Cannot delete team if ")
    @ParameterizedTest(name = "{0}")
    @MethodSource("$TEST_DATA_CLASSNAME#invalidTeamDeletion")
    @Suppress("UNUSED_PARAMETER")
    fun negativeDeleteTeamCases(testName: String, token: String?, teamId: Int?, expectedMsg: String?) {
        val resp = ApiClient.deleteTeam(token, teamId)

        assertAll(
            { checkValueEquality("the teams response array length", ApiClient.getTeams().body()?.size, 30) },
            { checkErrorMessage(resp, expectedMsg) }
        )
    }

    @Test
    fun debug() {
        DBClient().tryExposed()
    }
}
