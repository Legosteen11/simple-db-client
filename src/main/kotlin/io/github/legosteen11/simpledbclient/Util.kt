package io.github.legosteen11.simpledbclient

/**
 * Get the JDBC url for the given parameters.
 *
 * @param host The host name or ip-address of the database
 * @param database The database name
 * @param port The database port
 * @param driverName The driver name
 */
fun getJdbcUrl(host: String, port: String, database: String, driverName: String) = "jdbc:$driverName://$host:$port/$database"