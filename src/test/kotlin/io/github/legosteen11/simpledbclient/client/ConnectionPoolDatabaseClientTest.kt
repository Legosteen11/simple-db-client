package io.github.legosteen11.simpledbclient.client

/**
 * Created by wouter on 6/24/17.
 */
class ConnectionPoolDatabaseClientTest: DatabaseClientTestBase<ConnectionPoolDatabaseClient>() {
    override fun getClientInstance() = ConnectionPoolDatabaseClient(host, username, password, database, jdbcUrl = jdbcUrl)
}