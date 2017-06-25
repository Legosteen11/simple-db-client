package io.github.legosteen11.simpledbclient

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Get the JDBC url for the given parameters.
 *
 * @param host The host name or ip-address of the database
 * @param database The database name
 * @param port The database port
 * @param driverName The driver name
 */
fun getJdbcUrl(host: String, port: String, database: String, driverName: String) = "jdbc:$driverName://$host:$port/$database"

fun Connection.prepareStatement(sql: String, vararg parameters: Any?): PreparedStatement = prepareStatement(sql).apply {
    parameters.forEachIndexed { index, value ->
        setObject(index+1, value) // add one the index because it the first parameter is at position 1 and not 0.
    }
}

fun ResultSet.getFirstValue(): Any? = if(next()) {
    getObject(1) // get the first parameter if there are results
} else {
    null // return null because there are no results
}