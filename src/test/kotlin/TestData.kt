package com.example.sergeybibikov.kotlin.api_tests

import org.junit.jupiter.params.provider.Arguments

const val TEST_DATA_CLASSNAME = "com.example.sergeybibikov.kotlin.api_tests.TestDataKt"
fun successfulRegData(): Array<Arguments> {
    return arrayOf(
        Arguments.of("${getRandomString(4)}@gmail.co.uk"),
        Arguments.of("${getRandomString(4)}.test@gmail.com"),
        Arguments.of("${getRandomString(4)}-test@gmail.com"),
        Arguments.of("${getRandomString(4)}_test@gmail.com"),
        Arguments.of("${getRandomString(4)}3@gmail.com"),
    )
}

fun invalidEmailsData(): Array<Arguments> {
    return arrayOf(
        Arguments.of("mike234gmail.com", "does not have @ sign"),
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