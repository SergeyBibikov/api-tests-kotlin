package com.example.sergeybibikov.kotlin.api_tests.test_data

import com.example.sergeybibikov.kotlin.api_tests.api.ApiClient
import com.example.sergeybibikov.kotlin.api_tests.db.DBClient
import com.example.sergeybibikov.kotlin.api_tests.endpoints_data.GetTokenRequestBody
import com.example.sergeybibikov.kotlin.api_tests.utils.*
import org.junit.jupiter.params.provider.Arguments

const val TEST_DATA_CLASSNAME = "com.example.sergeybibikov.kotlin.api_tests.test_data.TestDataKt"
fun successfulRegData(): Array<Arguments> {
    return arrayOf(
        Arguments.of("${getRandomString(4)}@gmail.co.uk"),
        Arguments.of("${getRandomString(4)}@gmail.co.edu.uk"),
        Arguments.of("${getRandomString(4)}@g-mail.co.uk"),
        Arguments.of("${getRandomString(4)}.test@gmail.com"),
        Arguments.of("${getRandomString(4)}-test@gmail.com"),
        Arguments.of("${getRandomString(4)}_test@gmail.com"),
        Arguments.of("${getRandomString(4)}3@gmail.com"),
    )
}

fun invalidEmailsData(): Array<Arguments> {
    return arrayOf(
        Arguments.of("mike234gmail.com", "does not have @ sign"),
        Arguments.of("mikeу@gmail.com", "contains cyrillic letter"),
        Arguments.of(".abc@mail.com", "starts with a dot"),
        Arguments.of("_abc@mail.com", "starts with an underscore"),
        Arguments.of("-abc@mail.com", "starts with a dash"),
        Arguments.of("abc..def@mail.com", "prefix has several dots in a row"),
        Arguments.of("abc__def@mail.com", "prefix has several underscores in a row"),
        Arguments.of("abc--def@mail.com", "prefix has several dashes in a row"),
        Arguments.of("abc.@mail.com", "prefix ends with a dot"),
        Arguments.of("abc-@mail.com", "prefix ends with a dash"),
        Arguments.of("abc_@mail.com", "prefix ends with an underscore"),
        Arguments.of("a%bc@mail.com", "prefix has an invalid special character"),
        Arguments.of("abc@mail.c", "domain has only one letter after dot"),
        Arguments.of("abc@mail.comruu", "domain has 6 letters after dot"),
        Arguments.of("abc@ma%il.com", "domain contains an invalid special character"),
        Arguments.of("abc@mail..com", "domain has two dots in a row"),
        Arguments.of("abc@ma_il.com", "domain has an underscore"),
        Arguments.of("abc234124@mail.com.co.uk.some", "domain has 4 levels"),
    )
}

fun invalidPasswordData(): Array<Arguments> {
    return arrayOf(
        Arguments.of("a4dd", "is shorter then 8 chars", "The password must be at least 8 characters long"),
        Arguments.of("4asdfasdffdsdfsf", "has no uppercase letters", "default"),
        Arguments.of("Asdfasdffdsdfsf", "has no numbers", "default"),
        Arguments.of("23424245525", "has no letters", "default"),
    )
}

fun missingFieldsData(): Array<Arguments> {
    return arrayOf(
        Arguments.of(null, getValidPassword(), getValidEmail(), "username is missing"),
        Arguments.of(getValidUsername(), null, getValidEmail(), "password is missing"),
        Arguments.of(getValidUsername(), getValidPassword(), null, "email is missing"),
        Arguments.of(null, null, getValidEmail(), "username and password are missing"),
        Arguments.of(null, getValidPassword(), null, "username and email are missing"),
        Arguments.of(getValidUsername(), null, null, "password and email are missing"),
    )
}

fun conferencesData(): Array<Arguments> {
    return arrayOf(
        Arguments.of("East"),
        Arguments.of("West"),
        Arguments.of("east"),
        Arguments.of("west"),
        Arguments.of("eAst"),
        Arguments.of("wEst"),
    )
}

fun divisionsData(): Array<Arguments> {
    return arrayOf(
        Arguments.of("Atlantic"),
        Arguments.of("atlantic"),
        Arguments.of("atlaNtic"),
        Arguments.of("Pacific"),
        Arguments.of("pacific"),
        Arguments.of("paCific"),
        Arguments.of("Southeast"),
        Arguments.of("southeast"),
        Arguments.of("southeasT"),
        Arguments.of("Central"),
        Arguments.of("central"),
        Arguments.of("centraL"),
        Arguments.of("Northwest"),
        Arguments.of("northwest"),
        Arguments.of("norThwest"),
        Arguments.of("Southwest"),
        Arguments.of("southwest"),
        Arguments.of("souThwest"),
    )
}

fun roleAndUsername(): Array<Arguments> {
    val getQuery = { role: String ->
        """
            select u.* from users u 
                join roles r on r.id = u.roleid
            where r.name='$role'
        """.trimIndent()
    }
    val dbClient = DBClient()
    return arrayOf(
        Arguments.of("Admin", dbClient.getSingleUser(getQuery("Admin"))),
        Arguments.of("Regular", dbClient.getSingleUser(getQuery("Regular"))),
        Arguments.of("Premium", dbClient.getSingleUser(getQuery("Premium")))
    )
}

fun getTokenInvalidData(): Array<Arguments> {
    val invalidDataMsg = "invalid username and/or password"
    return arrayOf(
        Arguments.of(
            "the username is not from the DB",
            GetTokenRequestBody("SomeName", "1234"),
            invalidDataMsg
        ),
        Arguments.of(
            "the password is invalid",
            GetTokenRequestBody("Jack", "1234"),
            invalidDataMsg
        ),
        Arguments.of(
            "the username field is missing",
            GetTokenRequestBody(null, "1234"),
            "Username is a required field"
        ),
        Arguments.of(
            "the password field is missing",
            GetTokenRequestBody("Jack", null),
            "Password is a required field"
        ),

        )
}

fun validateTokenValidData(): List<Arguments> {

    val dbClient = DBClient()
    val users = dbClient.getSeveralUsers("select distinct on (roleid) * from users")
    val args: MutableList<Arguments> = mutableListOf()
    for (u in users) {
        val role = dbClient.getRole("select * from roles where id = ${u.roleId} limit 1")
        args.add(Arguments.of("${role?.name}_token_${u.username}"))
    }
    return args
}

fun validateTokenInvalidData(): Array<Arguments> {

    val dbClient = DBClient()
    val u = dbClient.getSingleUser("select * from users limit 1")
    val role = dbClient.getRole("select * from roles where id <> ${u.roleId} limit 1")

    val wrongRoleMsg = "incorrect user role"
    val tokenFormatMsg = "incorrect token format. Proper format: role_token_username"
    val usernameMsg = "invalid username"
    return arrayOf(
        Arguments.of("the user does not have this role", "${role?.name}_token_${u.username}", wrongRoleMsg),
        Arguments.of("the role does not exist", "dmin_token_Jack", wrongRoleMsg),
        Arguments.of("the username does not exist", "Admin_token_1324", usernameMsg),
        Arguments.of("underscore separates only the username", "Admintoken_Jack", tokenFormatMsg),
        Arguments.of("underscore separates only the role", "Admin_tokenJack", tokenFormatMsg),
        Arguments.of("no token provided", null, tokenFormatMsg),
    )
}

fun invalidTeamDeletion(): Array<Arguments> {
    val role = "Regular"
    val regularToken = getUserTokenWithRole(role)
    val adminToken = getUserTokenWithRole("Admin")
    val teamId = ApiClient.getTeams().body()?.get(0)?.id
    val invalidId = 100
    val formatMsg = "incorrect token format. Proper format: role_token_username"
    return arrayOf(
        Arguments.of("the user has role $role", regularToken, teamId, "user is not an admin"),
        Arguments.of("the token is not valid", "Sometoken", teamId, formatMsg),
        Arguments.of("the token is missing", "", teamId, formatMsg),
        Arguments.of("no team with id $invalidId", adminToken, invalidId, null),
    )
}