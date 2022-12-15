package com.example.sergeybibikov.kotlin.api_tests

import com.google.gson.Gson
import java.net.ConnectException
import kotlin.test.fail

/**
 * Waits for the service to start,
 * checks every 500 ms
 */
fun waitTillServiceIsUp(secsToWait: Int) {
    val iterations = secsToWait * 2
    for (i in 0..iterations) {
        try {
            ApiClient.ready()
        } catch (e: Exception) {
            if (e is ConnectException) {
                Thread.sleep(500L)
                continue
            }
        }
        return
    }
    fail("The service it not up after $secsToWait seconds")
}

fun getResponseErrorMessage(errorBodyString: String?): String {
    val g = Gson()
    val erObj = g.fromJson(errorBodyString, ApiError::class.java)
    return erObj.err
}

fun getRandomString(stringLen: Int, lettersOnly: Boolean = true): String {
    var chars = ('a'..'z') + ('A'..'Z')
    if (!lettersOnly) chars += ('0'..'9')
    var resultString = ""
    for (i in 1..stringLen){
        resultString += chars.random()
    }
    return resultString
}
