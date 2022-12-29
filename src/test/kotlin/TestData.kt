package com.example.sergeybibikov.kotlin.api_tests

import org.junit.jupiter.params.provider.Arguments

const val TEST_DATA_CLASSNAME = "com.example.sergeybibikov.kotlin.api_tests.TestDataKt"

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