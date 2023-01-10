package com.example.sergeybibikov.kotlin.api_tests

import org.junit.jupiter.params.provider.Arguments

const val TEST_DATA_CLASSNAME = "com.example.sergeybibikov.kotlin.api_tests.TestDataKt"
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
        Arguments.of("the username is not from the DB",
            GetTokenRequestBody("SomeName", "1234"),
            invalidDataMsg),
        Arguments.of("the password is invalid",
            GetTokenRequestBody("Jack", "1234"),
            invalidDataMsg),
        Arguments.of("the username field is missing",
            GetTokenRequestBody(null, "1234"),
            "Username is a required field"),
        Arguments.of("the password field is missing",
            GetTokenRequestBody("Jack", null),
            "Password is a required field"),

    )
}