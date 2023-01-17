package com.example.sergeybibikov.kotlin.api_tests.db

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.*
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

object RoleT : IntIdTable() {
    val name = varchar("name", 100)
}

object UserT : IntIdTable("users") {
    val username = varchar("username", 100)
    val email = varchar("email", 100)
    val password = varchar("password", 100)
    val roleid = integer("roleid").references(RoleT.id)
    val favPlayerId = integer("fav_playerid").nullable()
    val favTeamId = integer("fav_teamid").nullable()
}

class DBClient {
    private val host = "localhost"
    private val port = "5432"
    private val dbName = "postgres"

    fun tryExposed() {
        Database.connect(
            "jdbc:postgresql://${this.host}:${this.port}/${this.dbName}",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = System.getenv("DBPass")
        )

        transaction {
            UserT.selectAll().forEach {
                println("${it[UserT.id]}: ${it[UserT.username]}")
            }
        }
    }

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

    fun insert(insertStms: String): Int {
        getConnection().use { conn ->
            conn.createStatement().use { st ->
                st.execute(insertStms, Statement.RETURN_GENERATED_KEYS)
                st.generatedKeys.use {
                    it.next()
                    return it.getInt(1)
                }

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