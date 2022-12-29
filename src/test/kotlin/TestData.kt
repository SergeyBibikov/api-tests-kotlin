package com.example.sergeybibikov.kotlin.api_tests

const val TEST_DATA_CLASSNAME = "com.example.sergeybibikov.kotlin.api_tests.TestDataKt"

fun invalidEmailsData(): Array<Array<String>> {
    return arrayOf(
        arrayOf("mike234gmail.com", "does not have @ sign"),
        arrayOf(".abc@mail.com", "starts with a dot"),
        arrayOf("_abc@mail.com", "starts with an underscore"),
        arrayOf("-abc@mail.com", "starts with a dash"),
        arrayOf("abc..def@mail.com", "prefix has several dots in a row"),
        arrayOf("abc__def@mail.com", "prefix has several underscores in a row"),
        arrayOf("abc--def@mail.com", "prefix has several dashes in a row"),
        arrayOf("abc.@mail.com", "prefix ends with a dot"),
        arrayOf("abc-@mail.com", "prefix ends with a dash"),
        arrayOf("abc_@mail.com", "prefix ends with an underscore"),
        arrayOf("a%bc@mail.com", "prefix has an invalid special character"),
    )
}