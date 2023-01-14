package com.example.sergeybibikov.kotlin.api_tests.db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val roleId: Int,
    val favPlayerId: Int?,
)

data class Role(
    val id: Int,
    val name: String
)

class DBClient {
    private val host = "localhost"
    private val port = "5432"
    private val dbName = "postgres"


    fun getSingleUser(query: String): User {
        return getSeveralUsers(query)[0]
    }

    fun getSeveralUsers(query: String): List<User> {
        val result: MutableList<User> = mutableListOf()
        return executeQuery(query) {
            while (it.next()) {
                val favP = it.getInt(6)
                val u = User(
                    it.getInt(1),
                    it.getString(2),
                    it.getString(3),
                    it.getString(4),
                    it.getInt(5),
                    if (favP == 0) null else favP,
                )
                result.add(u)
            }
            result
        }
    }

    fun getRole(query: String): Role? {
        return executeQuery(query) {
            it.next()
            try {
                Role(it.getInt(1), it.getString(2))
            } catch (e: SQLException) {
                println(e.message)
                null
            }
        }
    }

    private inline fun <T> executeQuery(query: String, processResultSet: (ResultSet) -> T): T {
        getConnection().use {
            val stmt = it.createStatement()
            stmt.use { s ->
                val rs = s.executeQuery(query)
                rs.use {
                    return processResultSet(it)
                }
            }
        }
    }

    private fun getConnection(): Connection {
        val connString = "jdbc:postgresql://${this.host}:${this.port}/${this.dbName}"
        val props = Properties()
        props.setProperty("user", "postgres")
        props.setProperty("password", System.getenv("DBPass"))
        props.setProperty("ssl", "false")

        return DriverManager.getConnection(connString, props)
    }
}