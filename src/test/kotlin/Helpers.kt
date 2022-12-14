package com.example.sergeybibikov.kotlin.api_tests

import java.net.ConnectException
import kotlin.test.fail

class Helpers {
    companion object{
        fun waitTillServiceIsUp(secsToWait: Int) {
            val iterations = secsToWait * 2
            for (i in 0..iterations){
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
    }
}