package io.github.legosteen11.simpledbclient.client

import io.github.legosteen11.simpledbclient.IDatabaseClient
import io.github.legosteen11.simpledbclient.getJdbcUrl
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Simple database client that uses a plain JDBC connection.
 *
 * @param host The host name or ip-address of the database
 * @param username The database username
 * @param password The database password
 * @param database The database name
 * @param port The database port (default for MySQL, = 3306)
 * @param driverName The driver name for creating the JDBC url (default for MySQL, = mysql)
 * @param jdbcUrl The JDBC url (default with your credentials, = jdbc:[driverName]://[host]:[port]/[database])
 */
class SimpleDatabaseClient(host: String, username: String, password: String, database: String, port: String = "3306", driverName: String = "mysql", jdbcUrl: String = getJdbcUrl(host, port, database, driverName)) : IDatabaseClient {
    private val connection = java.sql.DriverManager.getConnection(jdbcUrl, username, password)

    override fun getConnection(): Connection = connection

    override fun executeQuery(sql: String, vararg parameters: Any?): ResultSet = prepareStatement(sql, *parameters).executeQuery()

    override fun executeUpdate(sql: String, vararg parameters: Any?): Int = prepareStatement(sql, *parameters).executeUpdate()

    override fun getSingleValueQuery(sql: String, vararg parameters: Any?): Any? = executeQuery(sql, *parameters).let {
        if(it.next()) {
            it.getObject(1) // get the first parameter if there are results
        } else {
            null // return null because there are no results
        }
    }

    private fun prepareStatement(sql: String, vararg parameters: Any?): PreparedStatement = getConnection().prepareStatement(sql).apply {
        parameters.forEachIndexed { index, value ->
            setObject(index+1, value) // add one the index because it the first parameter is at position 1 and not 0.
        }
    }
}