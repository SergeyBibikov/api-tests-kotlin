package com.example.sergeybibikov.kotlin.api_tests

import java.sql.Connection
import java.sql.DriverManager
import java.util.*

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val roleId: Int,
    val favPlayerId: Int?,
)

data class Role(val id: Int, val name: String)

class DBClient {
    private val host = "localhost"
    private val port = "5432"
    private val dbName = "postgres"

    fun getUserByUsername(username: String): User {
        val query = "select * from users where username = '$username'"
        return getSingleUser(query)
    }

    fun getRandomUser(): User {
        val query = "select * from users order by RANDOM() limit 1"
        return getSingleUser(query)
    }

    fun getRoleById(id: Int): Role {
        getConnection().use { conn ->
            val stmt = conn.createStatement()
            stmt.use { s ->
                val query = "select * from roles where id=$id"
                val rs = s.executeQuery(query)
                rs.use {
                    rs.next()
                    return Role(rs.getInt(1), rs.getString(2))
                }
            }
        }

    }

    private fun getSingleUser(query: String): User {
        getConnection().use {
            val stmt = it.createStatement()
            stmt.use { s ->
                val rs = s.executeQuery(query)
                rs.use { r ->
                    r.next()
                    val favP = r.getInt(6)
                    return User(
                        r.getInt(1),
                        r.getString(2),
                        r.getString(3),
                        r.getString(4),
                        r.getInt(5),
                        if (favP == 0) null else favP,
                    )
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