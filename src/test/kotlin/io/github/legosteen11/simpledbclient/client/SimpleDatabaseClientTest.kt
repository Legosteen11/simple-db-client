package io.github.legosteen11.simpledbclient.client

/**
 * Created by wouter on 6/23/17.
 */
class SimpleDatabaseClientTest: DatabaseClientTestBase<SimpleDatabaseClient>() {
    override fun getClientInstance() = SimpleDatabaseClient(host, username, password, database, jdbcUrl = jdbcUrl)
}