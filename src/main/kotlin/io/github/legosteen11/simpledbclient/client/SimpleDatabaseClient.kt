package io.github.legosteen11.simpledbclient.client

import io.github.legosteen11.simpledbclient.IDatabaseClient
import io.github.legosteen11.simpledbclient.getFirstValue
import io.github.legosteen11.simpledbclient.getJdbcUrl
import io.github.legosteen11.simpledbclient.prepareStatement
import java.sql.Connection
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

    override fun executeQuery(sql: String, vararg parameters: Any?): ResultSet = getConnection().prepareStatement(sql, *parameters).executeQuery()

    override fun executeUpdate(sql: String, vararg parameters: Any?, returnGeneratedKeys: Int?): Int {
        getConnection().prepareStatement(sql, *parameters, returnGeneratedKeys = returnGeneratedKeys).let {
            val rowsChanged = it.executeUpdate()

            if (returnGeneratedKeys == null) {
                return rowsChanged
            } else {
                it.generatedKeys.let {
                    if(it.next()) {
                        return it.getInt(1)
                    } else
                        return -1
                }
            }
        }
    }

    override fun getSingleValueQuery(sql: String, vararg parameters: Any?): Any? = executeQuery(sql, *parameters).getFirstValue()

    override fun disconnect() {
        connection.close()
    }

    override fun executeBatchUpdate(sql: String, vararg parameterArray: Array<Any?>, returnGeneratedKeys: Int?): Array<Int> {
        val statement = (if (returnGeneratedKeys == null) getConnection().prepareStatement(sql) else getConnection().prepareStatement(sql, returnGeneratedKeys)).apply {
            parameterArray.forEach {
                it.forEachIndexed { index, value ->
                    setObject(index+1, value)
                }
                addBatch()
            }
        }

        val rowsChanged = statement.executeBatch().toTypedArray()

        if(returnGeneratedKeys == null) {
            return rowsChanged
        } else {
            val genKeys = arrayListOf<Int>()

            statement.generatedKeys.let {
                while (it.next()) {
                    genKeys.add(it.getInt(1))
                }
            }

            return genKeys.toTypedArray()
        }
    }
}
